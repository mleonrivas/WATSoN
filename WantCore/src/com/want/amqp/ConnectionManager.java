package com.want.amqp;

import java.io.IOException;

import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.want.utils.ConfigurationProperties;

public class ConnectionManager {
	private static ConnectionManager instance=null;
	private Connection connection;
	private ConnectionFactory factory;
	
	private ConnectionManager() throws IOException{
		factory = new ConnectionFactory();
		factory.setHost(ConfigurationProperties.getInstance().getProperties().getProperty("rabbitmq_server", "127.0.0.1"));
		factory.setPort(new Integer(ConfigurationProperties.getInstance().getProperties().getProperty("rabbitmq_port", "5671")));
		connection = factory.newConnection();
	}
	
	public static synchronized ConnectionManager getInstance(){
		if(instance == null){
			try{
			instance = new ConnectionManager();
			}catch(Exception ex){
				ex.printStackTrace();
			}
		}
		return instance;
	}

	/**
	 * @return the connection
	 */
	public final Connection getConnection() {
		return connection;
	}

	/**
	 * @param connection the connection to set
	 */
	public final void setConnection(Connection connection) {
		this.connection = connection;
	}
	
	
	
}
