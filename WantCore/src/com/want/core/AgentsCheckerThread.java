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
	private Channel agentsChannel;

	//private Channel channel;

	private DefaultConsumer consumer;

	public AgentsCheckerThread(Channel channel) {
		agentsChannel = channel;
		initConnection();
	}

	private void initConnection() {
		try {

			isActive = true;
			//channel = ConnectionManager.getInstance().getAgentsChannel();
			consumer = new DefaultConsumer(agentsChannel) {
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
		System.out.println("******************** AGENTS CHECKER THREAD run()");
		while (isActive && !this.isInterrupted()) {
			//System.out.println("---------------- AgentsCheckerThread run");
			try {
				agentsChannel.basicConsume("agents", true, consumer);
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
			try {
				while (pingsStillAlive(pings)) {
					Thread.sleep(200);
				}

			
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
		close();
	}
}
