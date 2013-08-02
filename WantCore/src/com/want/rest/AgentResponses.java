package com.want.rest;

import org.restlet.resource.Get;

import com.want.core.Agent;
import com.want.core.Response;

public class AgentResponses extends BaseResource{
	
	@Get 
	public String responses(){
		String res = "";
		for(Agent a:getCoordinator().getAgentsConnected()){
			for(Response r : a.getReponses()){
				res = res + r.toString();
			}
		}
		return res;
	}
}
