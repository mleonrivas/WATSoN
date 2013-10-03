package com.want.core;

import java.io.IOException;
import java.util.Map;

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DefaultConsumer;
import com.rabbitmq.client.Envelope;
import com.want.amqp.ConnectionManager;
import com.want.factory.ResponsesFactory;

public class ResponseDispatcher extends Thread {
	
	private boolean isActive;
	
	private Channel channelResponses;
	
	private DefaultConsumer consumer;
	
	private Map<String, AgentRunner> runners;
	
	
	public ResponseDispatcher(Map<String, AgentRunner> rns){
		try {
			this.runners = rns;
			isActive = true;
			channelResponses = ConnectionManager.getInstance().getConnection().createChannel();
			channelResponses.queueDeclare("outputQueue", true, false, false, null);
			consumer = new DefaultConsumer(channelResponses) {
	        	public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties,
                		 byte[] body) throws IOException
                 {
	        		 String message = new String(body);
                     System.out.println("####handleDelivery: " + message);
                     Response response = ResponsesFactory.responseOfAgent(message);
                     //logs.add("[INFO]Received a response of: " + response.getId() +" "+ response.getAction());
	        		 if(runners.containsKey(response.getAgent())){
	        			 runners.get(response.getAgent()).notifyResponse(response);
	        		 }
	        		 EventRegistry.getInstance().notifyEvent(response);
                 }
		
		
	        };
		} catch (IOException ex) {
			ex.printStackTrace();
		}
	}	
	

	public void run(){
		while(isActive){
	        try {
				channelResponses.basicConsume("outputQueue", true, consumer);
			} catch (Exception ex) {
				ex.printStackTrace();
			}
			try {
				Thread.sleep(200);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
	
	public void closeDispatcher(){
		isActive = false;
		try {
			channelResponses.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
