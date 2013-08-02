package com.want.rest;

import java.util.List;
import java.util.concurrent.ConcurrentMap;

import org.restlet.resource.ServerResource;

import com.want.core.Agent;
import com.want.core.Coordinator; 

public class BaseResource extends ServerResource{

    
    protected Coordinator getCoordinator(){
    	return ((ResourceApplication) getApplication()).getCoordinator();
    }
    
    protected List<Agent> getAgentsConnected(){
    	return ((ResourceApplication) getApplication()).getAgentsConnected();
    }
  
}
