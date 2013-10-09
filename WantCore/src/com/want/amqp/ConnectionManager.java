package com.want.amqp;

import java.io.IOException;

import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.want.core.AgentsRegistry;
import com.want.core.IAgentData;
import com.want.utils.ConfigurationProperties;
import com.want.utils.DefaultProperties;
import com.rabbitmq.client.Channel;

public class ConnectionManager {
	private static ConnectionManager instance = null;
	private Connection connection;
	private ConnectionFactory factory;
	private Channel agentsChannel;
	private Channel messagesChannel;
	private Channel responsesChannel;
	private boolean isRetying;
	
	
	private ConnectionManager() {
		isRetying=false;
		factory = new ConnectionFactory();
		factory.setHost(ConfigurationProperties.getInstance().getProperties()
				.getProperty("rabbitmq_server",DefaultProperties.DEFAULT_RABBITMQ_HOST));
		factory.setPort(new Integer(ConfigurationProperties.getInstance()
				.getProperties().getProperty("rabbitmq_port", DefaultProperties.DEFAULT_RABBITMQ_PORT)));
		
		retryConfig();
	}

	public synchronized void retryConfig() {
		isRetying = true;
		while (isRetying) {
			try {
				connection = factory.newConnection();
				agentsChannel = connection.createChannel();
				agentsChannel.queueDeclare("agents", true, false, false, null);
				responsesChannel = connection.createChannel();
				responsesChannel.queueDeclare("outputQueue", true, false,false, null);
				messagesChannel = connection.createChannel();
				messagesChannel.exchangeDeclare(DefaultProperties.EXCHANGE_NAME, "topic");
				for(IAgentData a: AgentsRegistry.getInstance().getAllAgents()){
					messagesChannel.queueDeclare(DefaultProperties.AGENT_QUEUE_PATTERN + a.getId(), true, false, false, null);
					messagesChannel.queueBind(DefaultProperties.AGENT_QUEUE_PATTERN + a.getId(), DefaultProperties.EXCHANGE_NAME, a.getId());
				}
				
				isRetying = false;
			} catch (IOException e) {
				e.printStackTrace();
				try {
					Thread.sleep(500);
				} catch (InterruptedException e1) {
					e1.printStackTrace();
				}
			}
		}
	}

	public static synchronized ConnectionManager getInstance() {
		if (instance == null) {
			try {
				instance = new ConnectionManager();
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
		return instance;
	}

	/**
	 * @return the connection
	 */
	public final Connection getConnection() {
		return connection;
	}

	public Channel getAgentsChannel() {
		if(isRetying){
			throw new RuntimeException("Trying to restablish the connection with AMQP Server");
		}
		return agentsChannel;
	}

	public Channel getMessagesChannel() {
		if(isRetying){
			throw new RuntimeException("Trying to restablish the connection with AMQP Server");
		}
		return messagesChannel;
	}

	public Channel getResponsesChannel() {
		if(isRetying){
			throw new RuntimeException("Trying to restablish the connection with AMQP Server");
		}
		return responsesChannel;
	}

	public boolean isRetying() {
		return isRetying;
	}

	public synchronized void addAgentQueue(String agentId){
			try {
				messagesChannel.queueDeclare(DefaultProperties.AGENT_QUEUE_PATTERN + agentId, true, false, false, null);
				messagesChannel.queueBind(DefaultProperties.AGENT_QUEUE_PATTERN + agentId, DefaultProperties.EXCHANGE_NAME, agentId);		
			} catch (IOException e) {
				e.printStackTrace();
			}
			
	}
	
	public synchronized void removeAgentQueue(String agentId){
		
		try {
			messagesChannel.queueUnbind(DefaultProperties.AGENT_QUEUE_PATTERN + agentId, DefaultProperties.EXCHANGE_NAME, agentId);
			messagesChannel.queueDelete(DefaultProperties.AGENT_QUEUE_PATTERN + agentId);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
}

	

}
