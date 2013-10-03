package com.want.core;

import java.util.List;


public interface ICoordinator {
	
	List<IAgentData> getAllAgent();
	void play();
	void stop();
	void addScript(String script, String id);
	void addScript(String script);
	List<String> getScripts();
	List<IAgentData> getAgentsConnected();
	List<String> getServerLogs();
}
