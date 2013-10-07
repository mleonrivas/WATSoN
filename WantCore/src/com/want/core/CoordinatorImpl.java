package com.want.core;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.xml.bind.annotation.XmlRootElement;

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DefaultConsumer;
import com.rabbitmq.client.Envelope;
import com.want.amqp.ConnectionManager;
import com.want.factory.AgentFactory;
@XmlRootElement
public class CoordinatorImpl implements ICoordinator{
	

	

	private List<IAgentData> agentsConnected;

	private List<String> scripts;
	
	private List<String> logs;
	
	private Map<String, AgentRunner> agentsRunners;
	
	private Channel channelAgents;
	
	public CoordinatorImpl() throws IOException{
		
		
		agentsRunners = new HashMap<String, AgentRunner>();

		agentsConnected  = new ArrayList<IAgentData>();
		scripts = new ArrayList<String>();
		logs = new ArrayList<String>();
		EventRegistry.getInstance().resetRegistry(agentsRunners);
		ResponseDispatcher dispatcher = new ResponseDispatcher(agentsRunners);
		dispatcher.start();
	}
	@Override
	public List<String> getServerLogs(){
		return logs;
	}

	public List<String> getScripts(){
		return scripts;
	}

	@Override
	public List<IAgentData> getAllAgent() {
		List<PingAgent> pings = new LinkedList<PingAgent>();
		for(IAgentData a: agentsConnected){
			PingAgent ping = new PingAgent(a,this,null);
			pings.add(ping);
			ping.start();
		}
		
		try {
			
        	channelAgents = ConnectionManager.getInstance().getConnection().createChannel();
        	channelAgents.queueDeclare("agents", true, false, false, null);
        
        	final DefaultConsumer consumer = new DefaultConsumer(channelAgents) {
                 @Override
                 public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, 
                		 byte[] body) throws IOException
                 {             
                     String message = new String(body);
                     IAgentData agent= AgentFactory.getAgent(message);
                     agentsConnected.add(agent);
                     logs.add(agent.toString()); 
                 }
        	};
        	
        channelAgents.basicConsume("agents", true, consumer);
		} catch (Exception e){
			e.printStackTrace();
			try {
				channelAgents.close();
				
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}
		while(pingsStillAlive(pings)){
			try {
				Thread.sleep(500);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		
		return agentsConnected;
	}

	@Override
	public void play() {
		//getResponsesOfAgent();
		
		for(IAgentData a :agentsConnected){
			AgentRunner runner = new AgentRunner(a, this);
			agentsRunners.put(a.getId(), runner);
			runner.start();
		}
		EventRegistry.getInstance().resetRegistry(agentsRunners);
		
	}
	


	public List<IAgentData> getAgentsConnected() {
		
		return agentsConnected;
	}
	@Override
	public void stop() {
		
		
	}

	@Override
	public void addScript(String script, String id) {
		for (IAgentData a: agentsConnected){
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
	
	private boolean pingsStillAlive(List<PingAgent> pings){
		for(PingAgent ping: pings){
			if(ping.isAlive()){
				return true;
			}
		}
		return false;
	}
	

}
