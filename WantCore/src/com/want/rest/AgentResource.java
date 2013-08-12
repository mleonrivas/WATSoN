package com.want.rest;

import java.util.ArrayList;

import org.restlet.data.MediaType;
import org.restlet.representation.Representation;
import org.restlet.representation.StringRepresentation;
import org.restlet.resource.Get;

import com.want.core.Agent;

public class AgentResource extends BaseResource{
	
	@Get
	public String agents(){
		String res = "";
		for(Agent a:getAgentsConnected()){
			res = res + a.getId() + "\n";
		}
		return res;
	}
}
