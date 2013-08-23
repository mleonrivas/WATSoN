package com.want.core;

import java.util.List;

import com.want.utils.Action;


public interface Coordinator {
	
	List<Agent> getAllAgent();
	void play();
	void stop();
	void addScript(String script, String id);
	void addScript(String script);
	void getResponsesOfAgent();
	List<String> getScripts();
	List<Agent> getAgentsConnected();
	List<String> getServerLogs();
}
