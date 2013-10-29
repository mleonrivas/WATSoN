package com.want.core;

import java.io.IOException;

import com.rabbitmq.client.Channel;
import com.want.amqp.ConnectionManager;
import com.want.amqp.Message;
import com.want.utils.DefaultProperties;

public class MessageSenderThread extends Thread {
	
	private boolean isActive;
	private Channel messagesChannel;
	//private Channel channelSender;
	
	
	public MessageSenderThread(Channel channel){
		messagesChannel = channel;
		initConnection();
	}	
	
	private void initConnection(){
		isActive = true;
		//channelSender = ConnectionManager.getInstance().getMessagesChannel();
	}

	public void run(){
		System.out.println("******************** MESSAGE SENDER THREAD run()");
		while(isActive && !this.isInterrupted()){
			//System.out.println("**************** MessageSenderThread run");
			try {
				Message m = MessageQueue.getInstance().getQueue().take();
				if(AgentsRegistry.getInstance().existAgent(m.getRoutingKey())){
					messagesChannel.basicPublish(DefaultProperties.EXCHANGE_NAME, m.getRoutingKey(), null, m.getMessage().getBytes());
				}
				Thread.sleep(100);
			} catch (InterruptedException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
				checkConnection();
			}
		}
	}
	
	public void close(){
		isActive = false;
	}
	
	private void checkConnection(){
		if(!ConnectionManager.getInstance().isRetying()){
			System.out.println(" %%%%% MESSAGE SENDER START RETRY CONFIG %%%%% ");
			ConnectionManager.getInstance().retryConfig();	
		}
		close();
	}
	
}
