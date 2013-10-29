package com.want.core;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.xml.bind.annotation.XmlRootElement;

import com.want.amqp.ConnectionManager;
@XmlRootElement
public class CoordinatorImpl implements ICoordinator{
	

	private List<String> scripts;
	
	private List<String> logs;
	
	//private Map<String, AgentRunner> agentsRunners;
	
	
	public CoordinatorImpl() throws IOException{
		
		scripts = new ArrayList<String>();
		logs = new ArrayList<String>();
		ConnectionManager.getInstance().isRetying();
		
		
	}
	@Override
	public List<String> getServerLogs(){
		return logs;
	}

	public List<String> getScripts(){
		return scripts;
	}

	@Override
	public List<IAgentData> getAllAgent() {
		
		return AgentsRegistry.getInstance().getAllAgents();
	}

	@Override
	public void play() {
		Map<String,AgentRunner> runners = AgentsRegistry.getInstance().getAgentRunners();
		for(IAgentData a :AgentsRegistry.getInstance().getAllAgents()){
			AgentRunner runner = new AgentRunner(a);
			runners.put(a.getId(), runner);
			runner.start();
		}
		EventRegistry.getInstance().resetRegistry();
		
	}
	


	public List<IAgentData> getAgentsConnected() {
		return AgentsRegistry.getInstance().getAllAgents();
	}
	@Override
	public void stop() {
		
		
	}

	@Override
	public void addScript(String script, String id) {
		for (IAgentData a: AgentsRegistry.getInstance().getAllAgents()){
			if (a.getId().equals(id)){
				a.addAction(script);
				break;
			}
		}
	}

	@Override
	public void addScript(String script) {
		this.scripts.add(script);
	}
	
	
	

}
