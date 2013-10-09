package com.want.amqp;

public class Message {
	
	private String routingKey;
	private String message;
	
	
	public Message(String routingKey, String message) {
		super();
		this.routingKey = routingKey;
		this.message = message;
	}


	public String getRoutingKey() {
		return routingKey;
	}


	public void setRoutingKey(String routingKey) {
		this.routingKey = routingKey;
	}


	public String getMessage() {
		return message;
	}


	public void setMessage(String message) {
		this.message = message;
	}
	
	
}
