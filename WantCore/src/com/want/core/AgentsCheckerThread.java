package com.want.core;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DefaultConsumer;
import com.rabbitmq.client.Envelope;
import com.want.amqp.ConnectionManager;
import com.want.factory.AgentFactory;

public class AgentsCheckerThread extends Thread {

	private boolean isActive;

	private Channel channel;

	private DefaultConsumer consumer;

	public AgentsCheckerThread() {
		initConnection();
	}

	private void initConnection() {
		try {

			isActive = true;
			channel = ConnectionManager.getInstance().getAgentsChannel();
			consumer = new DefaultConsumer(channel) {
				@Override
				public void handleDelivery(String consumerTag,
						Envelope envelope, AMQP.BasicProperties properties,
						byte[] body) throws IOException {
					String message = new String(body);
					IAgentData agent = AgentFactory.getAgent(message);
					AgentsRegistry.getInstance().addAgent(agent);

				}

			};
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	public void run() {
		while (isActive) {
			try {
				channel.basicConsume("agents", true, consumer);
			} catch (Exception ex) {
				ex.printStackTrace();
				checkConnection();
			}

			List<PingAgent> pings = new LinkedList<PingAgent>();
			for (IAgentData a : AgentsRegistry.getInstance().getAllAgents()) {
				PingAgent ping = new PingAgent(a);
				pings.add(ping);
				ping.start();
			}
			while (pingsStillAlive(pings)) {
				try {
					Thread.sleep(200);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}

			try {
				Thread.sleep(2000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	public void close() {
		isActive = false;
	}

	private boolean pingsStillAlive(List<PingAgent> pings) {
		for (PingAgent ping : pings) {
			if (ping.isAlive()) {
				return true;
			}
		}
		return false;
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
