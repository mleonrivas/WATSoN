package com.want.utils;


import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class ConfigurationProperties {
	public static final String CURRENT_DOMAIN_PARSER = "com.want.domains.ice.ICEActionsParser";
	private static ConfigurationProperties instance=null;
	
	private Properties properties;
	
	private ConfigurationProperties(){
		properties = new Properties();
		properties.put("current_domain_parser", "com.want.domains.ice.ICEActionsParser");
		
	}
	
	public static synchronized ConfigurationProperties getInstance(){
		if(instance==null){
			instance = new ConfigurationProperties();
		}
		return instance;	
	}
	
	public void reload(){
		
		try {
			properties.load(this.getClass().getClassLoader().getResourceAsStream("config.properties"));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public Properties getProperties() {
		return properties;
	}


	
	
}
