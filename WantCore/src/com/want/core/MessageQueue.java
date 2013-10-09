package com.want.core;

import java.util.concurrent.LinkedBlockingQueue;

import com.want.amqp.Message;




public class MessageQueue {
	private static MessageQueue instance=null;
	
	private LinkedBlockingQueue<Message> queue;
	
	private MessageQueue(){
		queue = new LinkedBlockingQueue<Message>();
		
	}
	
	public static synchronized MessageQueue getInstance(){
		if(instance==null){
			instance = new MessageQueue();
		}
		return instance;	
	}
	
	public LinkedBlockingQueue<Message> getQueue(){
		return queue;
	}
	
	

	
	
}
