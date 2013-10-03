package com.want.domains.ice.actions;

import java.util.LinkedList;
import java.util.List;

import com.want.core.Action;
import com.want.utils.ConfigurationProperties;

public class InsertAsset implements IDomainAction{
	
	@Override
	public List<Action> getAgentActions(String id, String action,String param, String name,
			String data, String lastAction) {
		List<Action> result = new LinkedList<Action>();
		if(lastAction.equalsIgnoreCase("Insert Asset")){
			Action a1 = new Action();
			a1.setId(id + ".1");
			a1.setAction("click");
			a1.setLocalizator("xpath");
			a1.setLocalParam("//*[@class=\"controller\"][.=\"+\"]");
			a1.setConfiguration("");
			a1.setData("last");
			
			Action a2 = new Action();
			a2.setId(id + ".2");
			a2.setAction("wait");
			a2.setLocalizator("");
			a2.setLocalParam("");
			a2.setConfiguration("");
			a2.setData(ConfigurationProperties.getInstance().getProperties().getProperty("wait_time_after_click_tab", "2000"));
			result.add(a1);
			result.add(a2);
		}
		Action a3 = new Action();
		a3.setId(id + ".3");
		a3.setAction("changeValue");
		a3.setLocalizator("xpath");
		a3.setLocalParam("//*[@class = \"gwt-TextBox\"]");
		a3.setConfiguration(data);
		a3.setData("last");
		
		
		
		
		
		result.add(a3);
		return result;
	}

//	{
//		id: "1.19",
//		action: "changeValue",
//		localizator: "xpath",
//		localParam: '//*[@class = "gwt-TextBox"]',
//		configuration: "SMTP",
//		data: "last"
//	},
//	{
//		id: "1.20",
//		action: "click",
//		localizator: "xpath",
//		localParam: '//*[@class="controller"][.="+"]',
//		configuration: "",
//		data: "last"
//	},
//	{
//		id: "1.21",
//		action: "wait",
//		localizator: "",
//		localParam: "",
//		configuration: "",
//		data: "2000"
//	},
	
}
