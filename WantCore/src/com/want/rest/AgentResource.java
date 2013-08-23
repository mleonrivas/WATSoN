package com.want.rest;

import java.util.ArrayList;

import org.restlet.data.MediaType;
import org.restlet.representation.Representation;
import org.restlet.representation.StringRepresentation;
import org.restlet.resource.Get;

import com.want.core.Agent;

public class AgentResource extends BaseResource{
	
	String agent;
	public void init(){
		this.agent = (String) getRequestAttributes().get("agent");
	}
	
	@Get
	public String agents(){
		String res = "";
		for(Agent a:getAgentsConnected()){
			res = res + a.getId() + "\n";
		}
		return res;
	}
	
	@Get("/{agent}")
	public String anAgent(){
		String res = getCoordinator().getAgentsConnected().get(getCoordinator().getAgentsConnected().indexOf(agent)).toString();
		return res;
	}
	
}
