package com.want.domains.ice.actions;

import java.util.LinkedList;
import java.util.List;

import com.want.core.Action;
import com.want.utils.ConfigurationProperties;

public class ConfirmTask implements IDomainAction {

	@Override
	public List<Action> getAgentActions(String id, String action, String param, String name,
			String data, String lastAction) {
		
		List<Action> result = new LinkedList<Action>();
		Action a1 = new Action();
		a1.setId(id + ".1");
		a1.setAction("click");
		a1.setLocalizator("xpath");
		a1.setLocalParam("//*[@class=\"gwt-Button\"][.=\"Yes\"]");
		a1.setConfiguration("");
		a1.setData("0");
		Action a2 = new Action();
		a2.setId(id + ".2");
		a2.setAction("wait");
		a2.setLocalizator("");
		a2.setLocalParam("");
		a2.setConfiguration("");
		a2.setData(ConfigurationProperties.getInstance().getProperties().getProperty("wait_time_after_click_button", "3000"));
		result.add(a1);
		result.add(a2);
		
		return result;
		
	}

//	{
//		id: "36.3",
//		action: "click",
//		localizator: "xpath",
//		localParam: '//*[@class="gwt-Button"][.="Yes"]',
//		configuration: "",
//		data: "0"
//	},
//	{
//		id: "36.4",
//		action: "wait",
//		localizator: "",
//		localParam: "",
//		configuration: "",
//		data: "3000"
//	},
	
}
