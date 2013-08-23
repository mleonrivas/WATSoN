package com.want.rest;

import java.io.IOException;

import org.restlet.representation.Representation;
import org.restlet.resource.Delete;
import org.restlet.resource.Get;
import org.restlet.resource.Put;

import com.want.core.Agent;

public class DispenseScripts extends BaseResource{
	
	String agent;
	String action;
	
	public void init(){
//		this.action = (String) getRequestAttributes().get("action");
//		System.out.println(action);
		this.agent = (String) getRequestAttributes().get("agent");
	}

	
	@Put("/{agent}")
	public String scriptSelected(Representation entity) throws IOException{
		
		action = entity.getText();
		System.out.println(action);
		init();
		String res = "";
		for(Agent a : getCoordinator().getAgentsConnected()){
			if(a.getId().equals(agent)&&getCoordinator().getScripts().contains(action)){
				//a.addAction(action);
				getCoordinator().addScript(action, a.getId());
				res = a.getId()+ " " +a.getPendingActions().get(0);
			}else{
				System.out.println("Parametros de entrada invalidos");
				res = "Parametros de entrada invalidos";
			}
		}
		return res;
	}
	@Get
	public String sayTheChanges(){
		String res = "";
		for(Agent a: getCoordinator().getAgentsConnected()){
			res = res + a.getId()+ " will do it the tasks: ";
			for(String script : a.getPendingActions()){
				res = res + script + "\n";
			}
		}
		return res;
	}
	
	@Delete("/{agent}")
	public void scriptDelete(Representation entity) throws IOException{
		action = entity.getText();
		init();
		for(Agent a:getCoordinator().getAgentsConnected()){
			if(a.getId().equals(agent) && a.getPendingActions().contains(action)){
				a.getPendingActions().remove(action);
				System.out.println("An action is removed of agent "+a.getId());
			}else{
				System.out.println("Couldn't remove action of agent " +a.getId());
			}
		}
		
	}

}
