package com.want.rest;

import java.util.List;

import org.restlet.resource.ServerResource;

import com.want.core.IAgentData;
import com.want.core.ICoordinator;

public class BaseResource extends ServerResource{

    
    protected ICoordinator getCoordinator(){
    	return ((ResourceApplication) getApplication()).getCoordinator();
    }
    
    protected List<IAgentData> getAgentsConnected(){
    	return ((ResourceApplication) getApplication()).getAgentsConnected();
    }
  
}
