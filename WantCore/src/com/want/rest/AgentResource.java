package com.want.rest;

import org.restlet.resource.Get;

import com.want.core.AgentData;

public class AgentResource extends BaseResource{
	
	String agent;
	public void init(){
		this.agent = (String) getRequestAttributes().get("agent");
	}
	
	@Get
	public String agents(){
		String res = "";
		for(AgentData a:getAgentsConnected()){
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
