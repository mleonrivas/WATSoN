package com.want.factory;

import com.want.core.Agent;
import com.want.core.AgentImpl;

public class AgentFactory {
	public static Agent getAgent(String id, String browser, String browserVersion, String oS){
		Agent agent = new AgentImpl(id,browser, browserVersion,oS);
		return agent;
	}
	public static Agent getAgent(String byComma){
		String[] agent = byComma.split(",");
		String id = agent[0];
		String browser = agent[1];
		String browserVersion = agent[2];
		String oS = agent[3];
		Agent res = new AgentImpl(id,browser, browserVersion, oS); 
		System.out.println(res);
		return res;
	}
}
