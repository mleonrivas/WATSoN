package com.want.utils;


import java.io.IOException;
import java.util.Properties;

public class ConfigurationProperties {
	private static ConfigurationProperties instance=null;
	
	private Properties properties;
	
	private ConfigurationProperties(){
		properties = new Properties();
		reload();
		
	}
	
	public static synchronized ConfigurationProperties getInstance(){
		if(instance==null){
			instance = new ConfigurationProperties();
		}
		return instance;	
	}
	
	public void reload(){
		
		try {
			System.out.println();
			properties.load(this.getClass().getClassLoader().getResourceAsStream("../config.properties"));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public Properties getProperties() {
		return properties;
	}


	
	
}
