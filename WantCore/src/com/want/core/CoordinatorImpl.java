package com.want.core;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.xml.bind.annotation.XmlRootElement;
import com.rabbitmq.client.*;
import com.want.factory.AgentFactory;
import com.want.factory.ResponsesFactory;
import com.want.utils.*;
@XmlRootElement
public class CoordinatorImpl implements Coordinator{
	

	private ConnectionFactory factory = new ConnectionFactory();

	private List<Agent> agentsConnected;

	private List<String> scripts;

	private Map<Agent,List<String>> scriptAsigned;
	
	public CoordinatorImpl(String hostName){
		
		factory.setHost(hostName);

		scriptAsigned = new HashMap<Agent,List<String>>();
		agentsConnected  = new ArrayList<Agent>();
//		scripts = new ArrayList<String>();
//		File dir = new File("scripts");
//		String[] ficheros = dir.list();
//		String line = "[INFO] Files found: \n";
//		if (ficheros == null){
//			try{
//			  Logs.write("[ERROR] There are not files in directory");
//			}catch(IOException e){
//				e.printStackTrace();
//			}
//		}else { 
//			  for (int x=0;x<ficheros.length; x=x+1){
//				  scripts.add(ficheros[x]);
//				  line = line + ficheros[x] + "\n";
//			  }
//		}
//		System.out.println(line);
		scripts = new ArrayList<String>();

	}

	public List<String> getScripts(){
		return scripts;
	}

	@Override
	public List<Agent> getAllAgent() {

		try {
			Connection connection = factory.newConnection();
        	Channel channel = connection.createChannel();
        	channel.queueDeclare("agents", true, false, false, null);
        
        	final DefaultConsumer consumer = new DefaultConsumer(channel) {
                 @Override
                 public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, 
                		 byte[] body) throws IOException
                 {             
                     String message = new String(body);
                     Agent agent= AgentFactory.getAgent(message);
                     scriptAsigned.put(agent, new ArrayList<String>());
                     agentsConnected.add(agent);
                     Logs.write(agent.toString());
                     //System.out.println("######### handleDelivery: " + agent.toString()); 
                 }
        	};
        	
        channel.basicConsume("agents", true, consumer);
		} catch (ShutdownSignalException | ConsumerCancelledException
				| IOException e){
			e.printStackTrace();
		}
		return agentsConnected;
	}

	@Override
	public void play() {
		for (Agent a: scriptAsigned.keySet()){
			nextAction(a);
		}
	}
	
	public void nextAction(Agent a){
		if(scriptAsigned.get(a).isEmpty()){
			System.out.println("[INFO] Agent "+a.getId()+" have not more actions, "+a.getId()+" has finished.");
			try {
				Logs.write("[INFO] Agent "+a.getId()+" have not more actions, "+a.getId()+" has finished.");
			} catch (IOException e) {
				e.printStackTrace();
			}
		}else{
			String action = a.getPendingActions().get(0); //scriptAsigned.get(a).get(0);
			//FileImporter file = new FileImporter(action);
			a.sendMsg(action);
			//Message msg = new MessageImpl();
			//msg.sendMsg(file.getContent(), a.getId());
		}
		
	}

	public List<Agent> getAgentsConnected() {
		return agentsConnected;
	}
	@Override
	public void stop() {
		// TODO Auto-generated method stub	
	}

	@Override
	public void addScript(String script, String id) {
		Set<Agent> agents = scriptAsigned.keySet();
		Agent key = new AgentImpl();
		for (Agent a: agents){
			if (a.getId().equals(id)){
				key = a;
				break;
			}
		}
		scriptAsigned.get(key).add(script);
		key.setPendingActions(scriptAsigned.get(key));
		
	}

	@Override
	public void addScript(String script) {
		this.scripts.add(script);
	}

	@Override
	public void getResponsesOfAgent() {
		try {
			Connection connection = factory.newConnection();
			final Channel channel = connection.createChannel();
			channel.queueDeclare("outputQueue", true, false, false, null);

	        final DefaultConsumer consumer = new DefaultConsumer(channel) {
	                 @Override
	                 public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties,
	                		 byte[] body) throws IOException
	                 {
	                     String message = new String(body);
	                     Logs.write(message);
	                     System.out.println("######### handleDelivery: " + message);
	                     Response response = ResponsesFactory.responseOfAgent(message);
	                     
	                     if(response.getData().equals("true")){
	                    	 Set<Agent> agents = scriptAsigned.keySet();
	                    	 for(Agent a : agents){
	                    		if(a.getId().equals(response.getAgent())&&response.getAction().equals("end")){
	                    			if(!scriptAsigned.get(a).isEmpty()){
	                    				scriptAsigned.get(a).remove(0);
	                    				a.getPendingActions().remove(0);
	                    				a.addResponse(response);
	                    				nextAction(a);
	                    				break;
	                    			}else{
	                    				System.out.println("[INFO] Agent "+a.getId()+" have not more actions, "+a.getId()+" has finished.");
	                    				try {
	                    					Logs.write("[INFO] Agent "+a.getId()+" have not more actions, "+a.getId()+" has finished.");
	                    				} catch (IOException e) {
	                    					e.printStackTrace();
	                    				}
	                    			}
	                    		}else{
//	                    			System.out.println("[ERROR] Error on method getResponsesOfAgent() class CoordinatorImpl, the agent isn't the same");
//	                    			Logs.write("[ERROR] Error on method getResponsesOfAgent() class CoordinatorImpl, the agent isn't the same");
	                    		}
	                    	 }
	                     }else{
	                    	 System.out.println("[ERROR] Agent " + response.getAgent()+ " couldn't complete the action " + response.getId());
	                    	 Logs.write("[ERROR] Agent " + response.getAgent()+ " couldn't complete the action " + response.getId());
	                     }
	                 }
			};
	        channel.basicConsume("outputQueue", true, consumer);
		} catch (IOException e) {
			e.printStackTrace();
			System.out.println("######### Error on method \"getReponsesOfAgent()\" in class CoordinatorImpl");
		}
	}

}
