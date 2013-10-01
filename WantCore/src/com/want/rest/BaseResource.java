package com.want.rest;

import java.util.List;

import org.restlet.resource.ServerResource;

import com.want.core.AgentData;
import com.want.core.Coordinator;

public class BaseResource extends ServerResource{

    
    protected Coordinator getCoordinator(){
    	return ((ResourceApplication) getApplication()).getCoordinator();
    }
    
    protected List<AgentData> getAgentsConnected(){
    	return ((ResourceApplication) getApplication()).getAgentsConnected();
    }
  
}
