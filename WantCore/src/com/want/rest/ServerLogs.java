package com.want.rest;

import org.restlet.resource.Get;

public class ServerLogs extends BaseResource{
	
	@Get
	public String getLogs(){
		//Gson gson = new Gson();
		//return gson.toJson(getCoordinator().getServerLogs());
		
		String res = "";
		if(!getCoordinator().getServerLogs().isEmpty()){
			for(String s:getCoordinator().getServerLogs()){
				res = res + s + "\n";
			}
		}else{
			res = "there aren't logs from server.";
		}
		return res;
	}
}
