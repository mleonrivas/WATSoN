package com.want.core;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
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
public class CoordinatorImpl implements Coordinator{
	

	

	private List<AgentData> agentsConnected;

	private List<String> scripts;
	
	private List<String> logs;
	
	private Map<String, AgentRunner> agentsRunners;
	
	private Channel channelAgents;
	
	public CoordinatorImpl(String hostName) throws IOException{
		
		
		agentsRunners = new HashMap<String, AgentRunner>();

		agentsConnected  = new ArrayList<AgentData>();
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
	public List<AgentData> getAllAgent() {
		for(AgentData a: agentsConnected){
			PingAgent ping = new PingAgent(a,this,null);
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
                     AgentData agent= AgentFactory.getAgent(message);
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
		
		
		return agentsConnected;
	}

	@Override
	public void play() {
		//getResponsesOfAgent();
		
		for(AgentData a :agentsConnected){
			AgentRunner runner = new AgentRunner(a, this);
			agentsRunners.put(a.getId(), runner);
			runner.start();
		}
		EventRegistry.getInstance().resetRegistry(agentsRunners);
		ResponseDispatcher dispatcher = new ResponseDispatcher(agentsRunners);
		dispatcher.start();
	}
	


	public List<AgentData> getAgentsConnected() {
		
		return agentsConnected;
	}
	@Override
	public void stop() {
		
		
	}

	@Override
	public void addScript(String script, String id) {
		for (AgentData a: agentsConnected){
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
	

	

}
