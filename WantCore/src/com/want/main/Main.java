package com.want.main;

import org.restlet.Component;
import org.restlet.data.Protocol;

import com.want.rest.ResourceApplication;


public class Main {

	/**
	 * @param args
	 * @throws Exception 
	 * @throws Exception 
	 */ 
	public static void main(String[] args) throws Exception{
//		//create core
//		Coordinator coordinator = new CoordinatorImpl("localhost");
//		//add scripts
//		coordinator.getScripts();
//		//add agent connected
//		coordinator.getAllAgent();
//		//dispense actions
//		coordinator.addScript(coordinator.getScripts().get(0), coordinator.getAgentsConnected().get(0).getId());
//		//create responses connection
//		coordinator.getResponsesOfAgent();
//		//play
//		//coordinator.play();
		
		
		//REST INTERFACE
		// Create a new Component.
	    Component component = new Component();
	    // Add a new HTTP server listening on port 8182.
	    component.getServers().add(Protocol.HTTP, 8182);
	    // Attach the sample application.
	    component.getDefaultHost().attach("/want",
	            new ResourceApplication());
	    // Start the component.
	    component.start();
		//URL: http://localhost:8182/want/...
	}
	
}
