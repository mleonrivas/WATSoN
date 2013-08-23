package com.want.rest;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import org.restlet.Application;
import org.restlet.Restlet;
import org.restlet.routing.Router;

import com.want.core.Agent;
import com.want.core.Coordinator;
import com.want.core.CoordinatorImpl;
public class ResourceApplication extends Application{
	
		private final ConcurrentMap<String, List<String>> items = 
            new ConcurrentHashMap<String, List<String>>();
		private final Coordinator coordinator = new CoordinatorImpl("localhost");
		private List<Agent> agentConnected = coordinator.getAllAgent();
		
		public ResourceApplication(){
			//coordinator.getResponsesOfAgent();
		}
		
	 	@Override
	    public synchronized Restlet createInboundRoot() {
		

	        // Create a router Restlet that routes each call to a
	        // new instance of WebAplicatioNTesting.
	        Router router = new Router(getContext());
	          
	        // Defines the routes
	        router.attach("/files", FileResource.class);
	        router.attach("/agents", AgentResource.class);
	        router.attach("/play",Play.class);
	        router.attach("/add/{agent}", DispenseScripts.class);
	        router.attach("/responses/{agent}",AgentResponses.class);
	        router.attach("/fileUpload", UploadFile.class);
	        router.attach("/logs", ServerLogs.class);
	        router.attach("/stop", StopCore.class);
	        return router;
	    }
		/**
		 * Returns the list of registered items.
		 * 
		 * @return the list of registered items.
		 */
		public ConcurrentMap<String, List<String>> getItems() {
			return items;
		}
		public Coordinator getCoordinator(){
			return coordinator;
		}
		public List<Agent> getAgentsConnected(){
			return agentConnected;
		}
}
