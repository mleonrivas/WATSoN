package com.want.core;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.xml.bind.annotation.XmlRootElement;

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

	//private Map<Agent,List<String>> scriptAsigned;
	
	private Map<Agent,List<Integer>> waitingAgents;
	
	Connection connectionAgents;
	Connection connectionResponses;
	Channel channelAgents;
	Channel channelResponses;
	
	public CoordinatorImpl(String hostName){
		
		factory.setHost(hostName);
		waitingAgents = new HashMap<Agent,List<Integer>>();
		//scriptAsigned = new HashMap<Agent,List<String>>();
		agentsConnected  = new ArrayList<Agent>();
		scripts = new ArrayList<String>();

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
                     //scriptAsigned.put(agent, new ArrayList<String>());
                     agentsConnected.add(agent);
                     Logs.write(agent.toString());
                     //System.out.println("######### handleDelivery: " + agent.toString()); 
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

			for (Agent a: agentsConnected){
				nextAction(a);
			}
		
	}
	
	public void nextAction(Agent a){
		if(a.getPendingActions().isEmpty()){
			System.out.println("[INFO] Agent "+a.getId()+" have not more actions, "+a.getId()+" has finished.");
		}else{
			if(!a.itsWait()){
				String action = a.getPendingActions().get(0); 
				a.sendMsg(action);
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
		//Set<Agent> agents = scriptAsigned.keySet();
		//Agent key = new AgentImpl();
		for (Agent a: agentsConnected){
			if (a.getId().equals(id)){
				//key = a;
				a.addAction(script);
				break;
			}
		}
//		int index = agentsConnected.indexOf(key);
//		agentsConnected.get(index).addAction(script);
	}

	@Override
	public void addScript(String script) {
		this.scripts.add(script);
	}

	@Override
	public void getResponsesOfAgent() {
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
	                     System.out.println("######### handleDelivery: " + message);
	                     Response response = ResponsesFactory.responseOfAgent(message);
	                     
	                     if(response.getData().equals("true")){
	                    	 //Set<Agent> agents = scriptAsigned.keySet();
	                    	 for(Agent a : agentsConnected){
	                    		if(a.getId().equals(response.getAgent())){
	                    			a.addResponse(response);
	                    			if(response.getId().startsWith("R")){
	                    				String[] responseDates = response.getId().split(".");
	                    				if(waitingAgents.keySet().contains(a)){
	                    					waitingAgents.get(a).add(new Integer(responseDates[1]));
	                    				}else{
	                    					List<Integer> list = new ArrayList<Integer>();
	                    					list.add(new Integer(responseDates[1]));
	                    					waitingAgents.put(a, list);
	                    					a.setItsWait(true);
		                    				System.out.println(a.getId()+" en espera.");
	                    				}
	                    			}
	                    		} 
	                    		if(response.getAction().equals("end")){
	                    			String[] responseDates = response.getId().split(".");
	                    			for(Agent agent:waitingAgents.keySet()){
	                    				for(Integer i: waitingAgents.get(agent)){
	                    					if(i == new Integer(responseDates[0])){
	                    						waitingAgents.get(agent).remove(i);
	                    						if(waitingAgents.get(agent).isEmpty()){
	                    							agent.setItsWait(false);
	                    							System.out.println(agent.getId()+" it's not waiting now.");
	                    						}
	                    					}
	                    				}
	                    			}
	                    			if(!a.getPendingActions().isEmpty()){
	                    				a.getPendingActions().remove(0);
	                    				nextAction(a);
	                    				//break;
	                    			}else{
	                    				System.out.println("[INFO] Agent "+a.getId()+" have not more actions, "+a.getId()+" has finished.");
	                    			}
	                    		}
	                    	 }
	                     }else{
	                    	 System.out.println("[ERROR] Agent " + response.getAgent()+ " couldn't complete the action " + response.getId());
	                     }
	                 }
			};
			channelResponses.basicConsume("outputQueue", true, consumer);
		} catch (IOException e) {
			e.printStackTrace();
			System.out.println("######### Error on method \"getReponsesOfAgent()\" in class CoordinatorImpl");
			try {
				channelResponses.close();
				connectionResponses.close();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}
	}

}
