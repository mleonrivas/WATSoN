package com.want.core;

import java.util.List;


public interface Coordinator {
	
	List<AgentData> getAllAgent();
	void play();
	void stop();
	void addScript(String script, String id);
	void addScript(String script);
	List<String> getScripts();
	List<AgentData> getAgentsConnected();
	List<String> getServerLogs();
}
