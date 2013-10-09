package com.want.core;

import java.util.HashMap;
import java.util.Map;

public class EventRegistry {
	
	private static EventRegistry instance = null;
	
	private Map<String,Response> events;
	
	private EventRegistry(){
		events = new HashMap<String, Response>();
	}
	
	public static synchronized EventRegistry getInstance(){
		if(instance == null){
			instance = new EventRegistry();
		}
		return instance;
	}
	
	public void notifyEvent(Response response){
		events.put(response.getId(), response);
		Map<String,AgentRunner> runners = AgentsRegistry.getInstance().getAgentRunners();
		for(AgentRunner a: runners.values()){
			a.notifyEvent(response);
		}
	}
	
	public boolean existEvent(String event){
		return events.containsKey(event);
	}
	
	public void resetRegistry(){
		events = new HashMap<String, Response>();
		
	}
	
}
