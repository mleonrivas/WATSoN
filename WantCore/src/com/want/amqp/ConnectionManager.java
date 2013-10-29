package com.want.amqp;

import java.io.IOException;

import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.want.core.AgentsCheckerThread;
import com.want.core.AgentsRegistry;
import com.want.core.IAgentData;
import com.want.core.MessageSenderThread;
import com.want.core.ResponseDispatcherThread;
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
	private AgentsCheckerThread agentsChecker;
	private MessageSenderThread messageSender;
	private ResponseDispatcherThread responseDispatcher;
	private RecoverConnectionThread recoverConnectionThread;
	private ConnectionManager() {
		isRetying = false;
		factory = new ConnectionFactory();
		factory.setHost(ConfigurationProperties.getInstance().getProperties()
				.getProperty("rabbitmq_server",
						DefaultProperties.DEFAULT_RABBITMQ_HOST));
		factory.setPort(new Integer(ConfigurationProperties.getInstance()
				.getProperties().getProperty("rabbitmq_port",
						DefaultProperties.DEFAULT_RABBITMQ_PORT)));
		factory
				.setConnectionTimeout(Integer
						.parseInt(ConfigurationProperties.getInstance()
								.getProperties()
								.getProperty("rabbitmq_connection_timeout",
										DefaultProperties.DEFAULT_RABBITMQ_CONNECTION_TIMEOUT)));

		retryConfig();
	}

	public synchronized void retryConfig() {
		if(recoverConnectionThread==null || !recoverConnectionThread.isAlive() || recoverConnectionThread.isInterrupted()){
			recoverConnectionThread = new RecoverConnectionThread();
			recoverConnectionThread.start();
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
	public final synchronized Connection getConnection() {
		return connection;
	}

	/*public synchronized Channel getAgentsChannel() {
		if (isRetying) {
			throw new RuntimeException(
					"Trying to restablish the connection with AMQP Server");
		}
		if (!connection.isOpen()) {
			retryConfig();
		}
		return agentsChannel;
	}*/

	/*public synchronized Channel getMessagesChannel() {
		if (isRetying) {
			throw new RuntimeException(
					"Trying to restablish the connection with AMQP Server");
		}
		if (!connection.isOpen()) {
			retryConfig();
		}
		return messagesChannel;

	}*/

	/*public synchronized Channel getResponsesChannel() {
		if (isRetying) {
			throw new RuntimeException(
					"Trying to restablish the connection with AMQP Server");
		}
		if (!connection.isOpen()) {
			retryConfig();
		}
		return responsesChannel;
	}*/

	public boolean isRetying() {
		return isRetying;
	}

	public synchronized void addAgentQueue(String agentId) {
		try {
			messagesChannel.queueDeclare(DefaultProperties.AGENT_QUEUE_PATTERN
					+ agentId, true, false, false, null);
			messagesChannel.queueBind(DefaultProperties.AGENT_QUEUE_PATTERN
					+ agentId, DefaultProperties.EXCHANGE_NAME, agentId);
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	public synchronized void removeAgentQueue(String agentId) {

		try {
			messagesChannel.queueUnbind(DefaultProperties.AGENT_QUEUE_PATTERN
					+ agentId, DefaultProperties.EXCHANGE_NAME, agentId);
			messagesChannel.queueDelete(DefaultProperties.AGENT_QUEUE_PATTERN
					+ agentId);
		} catch (IOException e) {
			e.printStackTrace();
		}

	}
	
	public synchronized void closeThreads(){
		if (messageSender != null) {
			messageSender.close();
			messageSender.interrupt();
		}
		if (responseDispatcher != null) {
			responseDispatcher.close();
			responseDispatcher.interrupt();
		}
		if (agentsChecker != null) {
			agentsChecker.close();
			agentsChecker.interrupt();
		}
	}
	private class RecoverConnectionThread extends Thread {
		
		public void run(){
			isRetying = true;
			while (isRetying) {
				try {
					System.out.println("******************** close threads");
					closeThreads();
					System.out.println("******************** new connection");
					connection = factory.newConnection();
					System.out.println("******************** create Channels");
					agentsChannel = connection.createChannel();
					
					agentsChannel.queueDeclare("agents", true, false, false, null);
					System.out.println("******************** Agents channel number: " + agentsChannel.getChannelNumber());
					responsesChannel = connection.createChannel();
					responsesChannel.queueDeclare("outputQueue", true, false, false, null);
					System.out.println("******************** responses channel number: " + responsesChannel.getChannelNumber());
					messagesChannel = connection.createChannel();
					System.out.println("******************** msg channel number: " + messagesChannel.getChannelNumber());
					messagesChannel.exchangeDeclare(
							DefaultProperties.EXCHANGE_NAME, "topic");
					for (IAgentData a : AgentsRegistry.getInstance().getAllAgents()) {
						messagesChannel.queueDeclare(
								DefaultProperties.AGENT_QUEUE_PATTERN + a.getId(),
								true, false, false, null);
						messagesChannel.queueBind(
								DefaultProperties.AGENT_QUEUE_PATTERN + a.getId(),
								DefaultProperties.EXCHANGE_NAME, a.getId());
					}
					
					System.out.println("******************** Init threads");
					
					messageSender = new MessageSenderThread(messagesChannel);
					messageSender.start();
					
					responseDispatcher = new ResponseDispatcherThread(responsesChannel);
					responseDispatcher.start();
					
					agentsChecker = new AgentsCheckerThread(agentsChannel);
					agentsChecker.start();
					
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
		
	}
}
