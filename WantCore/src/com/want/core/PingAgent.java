package com.want.core;

import java.util.UUID;


public class PingAgent extends Thread{
	
	private IAgentData agent;
	private AgentRunner runner;
	
	public PingAgent(IAgentData a){
		agent = a;
		runner = AgentsRegistry.getInstance().getAgentRunners().get(agent.getId());
	}
	
	public void run(){
		System.out.println(" ### $$$ DOING PING TO " + agent.getId());
		if(runner!=null && runner.isAlive()){
			runner.setDoingPing(true);
		}
		Action a = new Action();
		String id = UUID.randomUUID().toString() + ".ping";
		a.setId(id);
		a.setConfiguration("");
		a.setData("");
		a.setLocalizator("");
		a.setLocalParam("");
		a.setAction("ping");
		agent.sendMsg(a.getJSON());
		int counter = 0;
		while(!EventRegistry.getInstance().existEvent(id) && counter<10){
			counter++;
			System.out.println(" ### $$$ COUNTING ... " + agent.getId());
			try {
				Thread.sleep(200);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		
		if(!EventRegistry.getInstance().existEvent(id)){
			System.out.println(" ### $$$ STOPPING " + agent.getId());
			AgentsRegistry.getInstance().removeAgent(agent.getId());
		}
		
		if(runner!=null && runner.isAlive()){
			runner.setDoingPing(false);
		}
		
		
	}
	
	
	
}
