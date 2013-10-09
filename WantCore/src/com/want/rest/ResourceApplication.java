package com.want.rest;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import org.restlet.Application;
import org.restlet.Restlet;
import org.restlet.routing.Router;

import com.want.core.IAgentData;
import com.want.core.ICoordinator;
import com.want.core.CoordinatorImpl;
public class ResourceApplication extends Application{
	
		private final ConcurrentMap<String, List<String>> items = 
            new ConcurrentHashMap<String, List<String>>();
		
			private final ICoordinator coordinator;
		
			//private List<IAgentData> agentConnected;
		
		public ResourceApplication() throws IOException{
			coordinator = new CoordinatorImpl();
			
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
		public ICoordinator getCoordinator(){
			return coordinator;
		}
		public List<IAgentData> getAgentsConnected(){
			return coordinator.getAllAgent();
		}
}
