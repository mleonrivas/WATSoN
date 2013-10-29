package com.want.domains.ice.actions;

import java.util.LinkedList;
import java.util.List;

import com.want.core.Action;
import com.want.utils.ConfigurationProperties;

public class OpenTask implements IDomainAction{

	@Override
	public List<Action> getAgentActions(String id, String action, String param, String name,
			String data, String lastAction) {
		
		List<Action> result = new LinkedList<Action>();
		
		Action a0 = new Action();
		a0.setId(id + ".1");
		a0.setAction("exist");
		a0.setLocalizator("xpath");
		a0.setLocalParam("//*[@class = \"TaskAsCard " + getTP(param) + " PID_" + getPid(param) + "\"]//*[@class = \"InlineAction\"][1]");
		a0.setConfiguration("");
		a0.setData("0");
		
		Action a1 = new Action();
		a1.setId(id + ".2");
		a1.setAction("click");
		a1.setLocalizator("xpath");
		a1.setLocalParam("//*[@class = \"TaskAsCard " + getTP(param) + " PID_" + getPid(param) + "\"]//*[@class = \"InlineAction\"][1]");
		a1.setConfiguration("");
		a1.setData("0");
		
		Action a2 = new Action();
		a2.setId(id + ".3");
		a2.setAction("wait");
		a2.setLocalizator("");
		a2.setLocalParam("");
		a2.setConfiguration("");
		a2.setData(ConfigurationProperties.getInstance().getProperties().getProperty("wait_time_after_click_button", "3000"));
		
		result.add(a0);
		result.add(a1);
		result.add(a2);
		
		return result;
	}
	
	private String getPid(String token){
		//1.1.5-48-TP2
		return token.split("-")[1];
	}
	private String getTP(String token){
		//1.1.5-48-TP2
		return token.split("-")[2];
	}

//	{
//		id:"1.0",
//		action: "exist",
//		localizator: "xpath",
//		localParam: '//*[@class = "TaskAsCard TP1 PID_36"]',
//		configuration: "",
//		data: "0"
//	},
//	{
//		id:"1.1",
//		action: "click",
//		localizator: "xpath",
//		localParam: '//*[@class = "TaskAsCard TP1 PID_36"]//*[@class = "InlineAction"][1]',
//		configuration: "",
//		data: "0"	
//	},
//	{
//		id: "1.2",
//		action: "wait",
//		localizator: "",
//		localParam: "",
//		configuration: "",
//		data: "3000"
//	},
	
	
}
