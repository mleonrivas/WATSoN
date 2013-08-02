package com.want.core;

import java.io.IOException;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

public class MessageImpl implements Message{
	
	public MessageImpl(){};
	@Override
	public void sendMsg(String message, String agentId) {
		ConnectionFactory factory = new ConnectionFactory();
		String finalMSG =agentId + "X." + message;
		Connection connection;
		try {
			connection = factory.newConnection();
			Channel channel = connection.createChannel();
			channel.queueDeclare("inputQueue", true, false, false, null);
			channel.basicPublish("","inputQueue", null, finalMSG.getBytes());
			channel.close();
			connection.close();
		} catch (IOException e) {
			e.printStackTrace();
			System.out.println("[ERROR] Error on method \"sendMsg()\" in class MessageImpl");
		}
	}

	@Override
	public void sendMsg(String message) {
		ConnectionFactory factory = new ConnectionFactory();
		String finalMSG = message;
		Connection connection;
		try {
			connection = factory.newConnection();
			Channel channel = connection.createChannel();
			channel.queueDeclare("inputQueue", true, false, false, null);
			channel.basicPublish("", "inputQueue", null, finalMSG.getBytes());
			channel.close();
			connection.close();
		} catch (IOException e) {
			e.printStackTrace();
			System.out.println("[ERROR] Error on method \"sendMsg()\" in class MessageImpl");
		}
	}

}
