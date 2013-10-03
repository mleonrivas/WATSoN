package com.want.factory;

import com.want.core.IAgentData;
import com.want.core.AgentDataImpl;

public class AgentFactory {
	public static IAgentData getAgent(String id, String browser, String browserVersion, String oS){
		IAgentData agent = new AgentDataImpl(id,browser, browserVersion,oS);
		return agent;
	}
	public static IAgentData getAgent(String byComma){
		String[] agent = byComma.split(",");
		String id = agent[0];
		String browser = agent[1];
		String browserVersion = agent[2];
		String oS = agent[3];
		IAgentData res = new AgentDataImpl(id,browser, browserVersion, oS); 
		System.out.println(res);
		return res;
	}
}
