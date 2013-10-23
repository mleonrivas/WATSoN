package com.want.core;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import com.want.amqp.ConnectionManager;

public class AgentsRegistry {
private static AgentsRegistry instance = null;
	
	private Map<String,AgentRunner> runners;
	
	private Map<String,IAgentData> agents;
	
	private AgentsRegistry(){
		agents = new HashMap<String, IAgentData>();
		runners = new HashMap<String, AgentRunner>();
	}
	
	public static synchronized AgentsRegistry getInstance(){
		if(instance == null){
			instance = new AgentsRegistry();
		}
		return instance;
	}
	
	public void addAgent(IAgentData agent){
		agents.put(agent.getId(), agent);
		ConnectionManager.getInstance().addAgentQueue(agent.getId());
	}
	
	public void removeAgent(String agentId){
		agents.remove(agentId);
		ConnectionManager.getInstance().removeAgentQueue(agentId);
		AgentRunner runner = runners.remove(agentId);
		if(runner!=null && runner.isAlive()){
			runner.setForceFinish(true);
			runner.interrupt();
		}
	}
	
	public List<IAgentData> getAllAgents(){
		List<IAgentData> result = new LinkedList<IAgentData>();
		result.addAll(agents.values());
		return result;
	}
	
	public Map<String,AgentRunner> getAgentRunners(){
		return runners;
	}
	
	public boolean existAgent(String agentId){
		return agents.containsKey(agentId);
	}
}
