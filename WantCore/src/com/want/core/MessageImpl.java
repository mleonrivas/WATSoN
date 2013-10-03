package com.want.core;

import java.io.IOException;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.want.amqp.ConnectionManager;

public class MessageImpl implements IMessage{
	
	public MessageImpl(){};
	@Override
	public void sendMsg(String message, String agentId) {
		
		String finalMSG =agentId + "X." + message;
		
		try {
			
			Channel channel = ConnectionManager.getInstance().getConnection().createChannel();
			channel.queueDeclare("inputQueue", true, false, false, null);
			channel.basicPublish("","inputQueue", null, finalMSG.getBytes());
			channel.close();
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
