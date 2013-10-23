package com.want.domains.ice.actions;

import java.util.LinkedList;
import java.util.List;

import com.want.core.Action;
import com.want.utils.ConfigurationProperties;
import com.want.utils.DefaultProperties;

public class ShowDeveloperMenu implements IDomainAction{

	@Override
	public List<Action> getAgentActions(String id, String action, String param, String name,
			String data, String lastAction) {
		
		List<Action> result = new LinkedList<Action>();
		
		
		Action a0 = new Action();
		a0.setId(id + ".1");
		a0.setAction("wait");
		a0.setLocalizator("");
		a0.setLocalParam("");
		a0.setConfiguration("");
		a0.setData(ConfigurationProperties.getInstance().getProperties().getProperty("wait_time_after_click_button", DefaultProperties.DEFAULT_WAIT_TIME_AFTER_CLICK_BUTTON));
		Action a1 = new Action();
		a1.setId(id + ".1");
		a1.setAction("keydown");
		a1.setLocalizator("xpath");
		a1.setLocalParam("/html/body");
		a1.setConfiguration("{key: \"M\", shiftPressed: true, altPressed: false, ctrlPressed: true}");
		a1.setData("0");
		Action a2 = new Action();
		a2.setId(id + ".4");
		a2.setAction("wait");
		a2.setLocalizator("");
		a2.setLocalParam("");
		a2.setConfiguration("");
		a2.setData(ConfigurationProperties.getInstance().getProperties().getProperty("wait_time_after_click_button", DefaultProperties.DEFAULT_WAIT_TIME_AFTER_CLICK_BUTTON));

		result.add(a0);
		result.add(a1);
		result.add(a2);
		
		return result;
	}
	
	
	
	
}
