package com.want.rest;

import org.restlet.resource.Get;

import com.want.core.Action;
import com.want.core.IAgentData;


public class FileResource extends BaseResource{
	
	
	@Get
	public String actions(){
	
		String res = "";
		for(IAgentData a : getCoordinator().getAgentsConnected()){
			for(Action s: a.getPendingActions()){
				res = res +"\n-----------------\n "+ s.getJSON();
			}
		}
		return res;
	}

}
