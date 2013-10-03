package com.want.domains.ice.actions;

import java.util.LinkedList;
import java.util.List;

import com.want.core.Action;

public class SelectOption implements IDomainAction {

	@Override
	public List<Action> getAgentActions(String id, String action, String param, String name,
			String data, String lastAction) {
		
		List<Action> result = new LinkedList<Action>();
		String value = param.equalsIgnoreCase("Yes")? "true" : "false";
		Action a1 = new Action();
		a1.setId(id + ".1");
		a1.setAction("click");
		a1.setLocalizator("xpath");
		a1.setLocalParam("//*[@class = \"gwt-RadioButton\"]/input[@value=\"" + value + "\"][@name=\"" + name + "\"]");
		a1.setConfiguration("");
		a1.setData("0");
		
		result.add(a1);

		return result;
	}

//	{
//		id: "4.3",
//		action: "click",
//		localizator: "xpath",
//		localParam: '//*[@class = "gwt-RadioButton"]/input[@value="true"][@name="HTTPS"]',
//		configuration: "",
//		data: "0"
//	},
	
}
