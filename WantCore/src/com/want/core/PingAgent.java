package com.want.core;

import java.util.UUID;


public class PingAgent extends Thread{
	
	private AgentData agent;
	private AgentRunner runner;
	private Coordinator coordinator;
	
	public PingAgent(AgentData a, Coordinator c, AgentRunner r){
		agent = a;
		coordinator = c;
		runner = r;
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
		agent.sendMsg(a.getJSON());
		int counter = 0;
		while(!EventRegistry.getInstance().existEvent(id) && counter<10){
			counter++;
			System.out.println(" ### $$$ COUNTING ... " + agent.getId());
			try {
				Thread.sleep(500);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		
		if(!EventRegistry.getInstance().existEvent(id)){
			System.out.println(" ### $$$ STOPPING " + agent.getId());
			if(runner!=null && runner.isAlive()){
					runner.interrupt();
			}
			coordinator.getAgentsConnected().remove(agent);
		}
		
		if(runner!=null && runner.isAlive()){
			runner.setDoingPing(false);
		}
		
		
	}
	
	
	
}
