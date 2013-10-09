package com.want.core;

import java.io.IOException;
import java.util.Map;

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DefaultConsumer;
import com.rabbitmq.client.Envelope;
import com.want.amqp.ConnectionManager;
import com.want.factory.ResponsesFactory;

public class ResponseDispatcherThread extends Thread {

	private boolean isActive;

	private Channel channelResponses;

	private DefaultConsumer consumer;

	public ResponseDispatcherThread() {
		initConnection();
	}

	private void initConnection() {
		try {

			isActive = true;
			channelResponses = ConnectionManager.getInstance()
					.getResponsesChannel();
			consumer = new DefaultConsumer(channelResponses) {
				public void handleDelivery(String consumerTag,
						Envelope envelope, AMQP.BasicProperties properties,
						byte[] body) throws IOException {
					String message = new String(body);
					System.out.println("####handleDelivery: " + message);
					Response response = ResponsesFactory
							.responseOfAgent(message);
					// logs.add("[INFO]Received a response of: " +
					// response.getId() +" "+ response.getAction());
					Map<String, AgentRunner> runners = AgentsRegistry
							.getInstance().getAgentRunners();
					if (runners.containsKey(response.getAgent())) {
						runners.get(response.getAgent()).notifyResponse(
								response);
					}
					EventRegistry.getInstance().notifyEvent(response);
				}

			};
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	public void run() {
		while (isActive) {
			try {
				channelResponses.basicConsume("outputQueue", true, consumer);
			} catch (Exception ex) {
				ex.printStackTrace();
				checkConnection();
			}
			try {
				Thread.sleep(200);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	public void close() {
		isActive = false;

	}
	
	private void checkConnection(){
		if(!ConnectionManager.getInstance().isRetying()){
			System.out.println(" %%%%% AGENTS CHECKER START RETRY CONFIG %%%%% ");
			ConnectionManager.getInstance().retryConfig();	
		}
		while(ConnectionManager.getInstance().isRetying()){
			try {
				Thread.sleep(500);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		initConnection();
	}
}