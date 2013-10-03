package com.want.rest;

import java.util.ArrayList;
import java.util.List;

import org.restlet.resource.Get;

import com.want.core.IAgentData;
import com.want.core.Response;

public class AgentResponses extends BaseResource{
	String agent;
	
	public void init(){
		this.agent = (String) getRequestAttributes().get("agent");
	}
	
	@Get ("/{agent}")
	public String responses(){
		init();
		String res = "";
		//getCoordinator().getResponsesOfAgent();
		
		List<String> agents = new ArrayList<String>();
		for(IAgentData a: getCoordinator().getAgentsConnected()){
			agents.add(a.getId());
		}
		
		if(!agents.contains(agent)){
			res = "this agent not exist or not is connected.";
		}else{
//			int index = getCoordinator().getAgentsConnected().indexOf(agent);
//			Agent agent = getCoordinator().getAgentsConnected().get(index);
//			if(!agent.getReponses().isEmpty()){
//				for(Response response: agent.getReponses()){
//					res = res + response.getId()+" "+response.getAction()+" "+response.getData()+"\n";
//				}
//			}else{
//				res = "In this agent there isn't responses";
//			}
			for(IAgentData a: getCoordinator().getAgentsConnected()){
				if(a.getId().equals(agent)){
					if(!a.getReponses().isEmpty()){
						for(Response response: a.getReponses()){
							res = res + response.getId()+" "+response.getAction()+" "+response.getData()+"\n";
						}
					}else{
						res = "this agent has not responded because it hasn't assigned action.";
					}
				}
			}
		}
		System.out.println(res);
		return res;
	}
}
