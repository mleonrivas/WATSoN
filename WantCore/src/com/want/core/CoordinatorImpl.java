package com.want.core;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.xml.bind.annotation.XmlRootElement;

import sun.management.resources.agent;
import sun.net.ConnectionResetException;

import com.rabbitmq.client.*;
import com.want.factory.AgentFactory;
import com.want.factory.ResponsesFactory;
import com.want.utils.*;
@XmlRootElement
public class CoordinatorImpl implements Coordinator{
	

	private ConnectionFactory factory = new ConnectionFactory();

	private List<Agent> agentsConnected;

	private List<String> scripts;
	
	private List<String> doneTasks;
	
	private List<String> logs;
	
	private Map<Agent,String> waitAgent;
	
	Connection connectionAgents;
	Connection connectionResponses;
	Channel channelAgents;
	Channel channelResponses;
	
	public CoordinatorImpl(String hostName){
		
		factory.setHost(hostName);
		waitAgent = new HashMap<Agent,String>();
		doneTasks = new ArrayList<String>();
		agentsConnected  = new ArrayList<Agent>();
		scripts = new ArrayList<String>();
		logs = new ArrayList<String>();
		
	}
	@Override
	public List<String> getServerLogs(){
		return logs;
	}

	public List<String> getScripts(){
		return scripts;
	}

	@Override
	public List<Agent> getAllAgent() {

		try {
			connectionAgents = factory.newConnection();
        	channelAgents = connectionAgents.createChannel();
        	channelAgents.queueDeclare("agents", true, false, false, null);
        
        	final DefaultConsumer consumer = new DefaultConsumer(channelAgents) {
                 @Override
                 public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, 
                		 byte[] body) throws IOException
                 {             
                     String message = new String(body);
                     Agent agent= AgentFactory.getAgent(message);
                     agentsConnected.add(agent);
                     logs.add(agent.toString()); 
                 }
        	};
        	
        channelAgents.basicConsume("agents", true, consumer);
		} catch (ShutdownSignalException | ConsumerCancelledException
				| IOException e){
			e.printStackTrace();
			try {
				channelAgents.close();
				connectionAgents.close();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}
		return agentsConnected;
	}

	@Override
	public void play() {
		//getResponsesOfAgent();
		for(Agent a :agentsConnected){
			a.setIndexOfScript(0);
			nextAction(a,a.getIndexOfScript());
		}
	}
	
	public void nextAction(final Agent a, final int indexAction){
		if(!a.getPendingActions().isEmpty() && !a.itsWait()){
			String action = a.getPendingActions().get(indexAction);
			a.sendMsg(action);
			try {
				connectionResponses = factory.newConnection();
				channelResponses = connectionResponses.createChannel();
				channelResponses.queueDeclare("outputQueue", true, false, false, null);

		        final DefaultConsumer consumer = new DefaultConsumer(channelResponses) {
		                 @Override
		                 public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties,
		                		 byte[] body) throws IOException
		                 {
		                     String message = new String(body);
		                     System.out.println("####handleDelivery: " + message);
		                     Response response = ResponsesFactory.responseOfAgent(message);
		                     System.out.println("####he recibido una respuesta: " + response.getId() +" "+ response.getAction() );
		                     logs.add("[INFO]Received a response of: " + response.getId() +" "+ response.getAction());
		                     for(Agent a : agentsConnected){
		                    		if(a.getId().equals(response.getAgent())){
		                    			System.out.println("####identificado agente "+a.getId());
		                    			a.addResponse(response);
		                    			
		                    			if(response.getAction().equals("waitUntil")){
			                    			System.out.println("####procesando waitUntil, agente en espera");
			                    			//System.out.println("####comprobando identificacion de respuesta: "+response.getId().split(".")[1]);
			                    			a.setItsWait(true);
						                    waitAgent.put(a, (String) response.getId().subSequence(2, 3));
			                    			
					                    }
		                    			if(response.getAction().equals("end")){
			                    			System.out.println("####procesando end");
			                    			String splt = (String) response.getId().subSequence(0, 1);
			                    			a.setIndexOfScript(a.getIndexOfScript()+1);
			                    				System.out.println("####viene el for");
			                    				doneTasks.add(splt);
			                    				for(Agent ag : waitAgent.keySet()){
						                    		System.out.println("####dentro del for "+ waitAgent.get(ag));
						                    		System.out.println("#### agente y numero asociado" + ag.toString() + " no: "+splt);
						                    		if(waitAgent.get(ag).equals(splt)){
						                    			agentsConnected.get(agentsConnected.indexOf(ag)).setItsWait(false);
						                    			System.out.println("####agente liberado de la espera");
						                    			System.out.println("####indice de accion: "+ag.getIndexOfScript());
						                    			nextAction(agentsConnected.get(agentsConnected.indexOf(ag)), ag.getIndexOfScript());
						                    			System.out.println("####lanzando next action");
						                    			waitAgent.remove(ag);
						                    		}
						                    	}
					                    	if(a.getPendingActions().size()>a.getIndexOfScript() && !a.itsWait()){
					                    		System.out.println("####lanzando siguiente accion");
					                    		logs.add("[INFO]Throw next action of " + a.getId());
					                 			nextAction(a, a.getIndexOfScript());
					                 		} 
					                    }
		                    		}	
		                     }
		                     
		                     if(response.getData().equals("false")){
		                    	 System.out.println("[ERROR] Agent " + response.getAgent()+ " couldn't complete the action " + response.getId());
		                     }else{
		                    	 System.out.println("[INFO] Agent " + response.getAgent()+ " complete the action " + response.getId());
		                     }
		                     
		                 }
				};
				channelResponses.basicConsume("outputQueue", true, consumer);
			} catch (IOException e) {
				e.printStackTrace();
				System.out.println("Error on method \"nextAction()\" in class CoordinatorImpl");
			}
		}
	}


	public List<Agent> getAgentsConnected() {
		return agentsConnected;
	}
	@Override
	public void stop() {
		
		try {
			channelAgents.close();
			channelResponses.close();
		} catch (IOException e) {
			System.out.println("Can't close connection's channels");
			e.printStackTrace();
		}
	}

	@Override
	public void addScript(String script, String id) {
		for (Agent a: agentsConnected){
			if (a.getId().equals(id)){
				a.addAction(script);
				break;
			}
		}
	}

	@Override
	public void addScript(String script) {
		this.scripts.add(script);
	}
	

	@Override
	public void getResponsesOfAgent() {
//		
//		try {
//			connectionResponses = factory.newConnection();
//			 channelResponses = connectionResponses.createChannel();
//			 channelResponses.queueDeclare("outputQueue", true, false, false, null);
//
//	        final DefaultConsumer consumer = new DefaultConsumer(channelResponses) {
//	                 @Override
//	                 public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties,
//	                		 byte[] body) throws IOException
//	                 {
//	                     String message = new String(body);
//	                     System.out.println("######### handleDelivery: " + message);
//	                     Response response = ResponsesFactory.responseOfAgent(message);
//	                     System.out.println(" herecibido una respuesta: " + response.getId() +" "+ response.getAction() );
//	                     
//	                     for(Agent a : agentsConnected){
//	                    		if(a.getId().equals(response.getAgent())){
//	                    			a.addResponse(response);
//	                    			
//	                    			if(response.getAction().equals("end")){
//	                    				
//	                    				System.out.println(" he recibido un end: " + response.getAction());
//	                    				
//		                    			String[] responseDates = response.getId().split(".");
//		                    			for(Agent agent:waitingAgents.keySet()){
//		                    				for(Integer i: waitingAgents.get(agent)){
//		                    					if(i == new Integer(responseDates[0])){
//		                    						System.out.println("exixte tarea de espera asocada");
//		                    						waitingAgents.get(agent).remove(i);
//		                    						if(waitingAgents.get(agent).isEmpty()){
//		                    							System.out.println("devolviendo a estado activo");
//		                    							agent.setItsWait(false);
//		                    							System.out.println(agent.getId()+" it's not waiting now.");
////		                    							nextAction(agentsConnected.indexOf(a));
//		                    						}
//		                    					}
//		                    				}
//		                    			}
//		                    			
//		                    			if(a.getPendingActions().isEmpty()){
//		                    				System.out.println("[INFO] Agent "+a.getId()+" have not more actions, "+a.getId()+" has finished.");
//		                    			}
//		                    			System.out.println(" elimmino el primer script");
//	                    				a.getPendingActions().remove(0);
////	                    				System.out.println(" lanzando siguiente accion");
////	                    				nextAction(agentsConnected.indexOf(a));
//		                    		}
//	                    			
//	                    			if(response.getId().startsWith("R")){
//	                    				System.out.println(" detectada espera " +response.getAction()+" "+response.getId()+" "+response.getAgent());
//	                    				String[] responseDates = response.getId().split(".");
//	                    				if(waitingAgents.keySet().contains(a)){
//	                    					waitingAgents.get(a).add(new Integer(responseDates[1]));
//	                    					System.out.println(a.getId()+" en espera.");
//	                    				}else{
//	                    					List<Integer> list = new ArrayList<Integer>();
//	                    					list.add(new Integer(responseDates[1]));
//	                    					waitingAgents.put(a, list);
//	                    					a.setItsWait(true);
//	                    					System.out.println(a.getId()+" en espera.");
//	                    				}
//	                    				a.getPendingActions().remove(0);
//	                    			}
//	                    			
//	                    		}
//	                     }
//	                     if(response.getData().equals("true")){
//	                    	 System.out.println("[INFO] Agent " + response.getAgent()+ " complete the action " + response.getId());
//	                     }else{
//	                    	 System.out.println("[ERROR] Agent " + response.getAgent()+ " couldn't complete the action " + response.getId());
//	                     }
//	                 }
//			};
//			channelResponses.basicConsume("outputQueue", true, consumer);
////			channelResponses.close();
////			connectionResponses.close();
//		} catch (IOException e) {
//			e.printStackTrace();
//			System.out.println("######### Error on method \"getReponsesOfAgent()\" in class CoordinatorImpl");
//		}
	}

}
