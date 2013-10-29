package com.want.domains.ice.actions;

import java.util.LinkedList;
import java.util.List;

import com.want.core.Action;
import com.want.utils.ConfigurationProperties;
import com.want.utils.DefaultProperties;

public class LaunchMenuOption implements IDomainAction{

	@Override
	public List<Action> getAgentActions(String id, String action, String param, String name,
			String data, String lastAction) {
		
		List<Action> result = new LinkedList<Action>();
		
		Action a0 = new Action();
		a0.setId(id + ".1");
		a0.setAction("exist");
		a0.setLocalizator("xpath");
		a0.setLocalParam("//*[@class=\"settingsLabel\"]");
		a0.setConfiguration("");
		a0.setData("0");
		
		Action a1 = new Action();
		a1.setId(id + ".2");
		a1.setAction("click");
		a1.setLocalizator("xpath");
		a1.setLocalParam("//*[@class=\"settingsLabel\"]");
		a1.setConfiguration("");
		a1.setData("0");
		
		Action a2 = new Action();
		a2.setId(id + ".3");
		a2.setAction("wait");
		a2.setLocalizator("");
		a2.setLocalParam("");
		a2.setConfiguration("");
		a2.setData(ConfigurationProperties.getInstance().getProperties().getProperty("wait_time_after_click_tab", DefaultProperties.DEFAULT_WAIT_TIME_AFTER_CLICK_TAB));
		
		Action a3 = new Action();
		a3.setId(id + ".3");
		a3.setAction("click");
		a3.setLocalizator("xpath");
		a3.setLocalParam("//*[@class=\"SettingsView\"]/div[contains(text(),\"" + param + "\")]");
		a3.setConfiguration("");
		a3.setData("0");
		
		Action a4 = new Action();
		a4.setId(id + ".4");
		a4.setAction("wait");
		a4.setLocalizator("");
		a4.setLocalParam("");
		a4.setConfiguration("");
		a4.setData(ConfigurationProperties.getInstance().getProperties().getProperty("wait_time_after_click_button", DefaultProperties.DEFAULT_WAIT_TIME_AFTER_CLICK_BUTTON));
		
		
		result.add(a0);
		result.add(a1);
		result.add(a2);
		result.add(a3);
		result.add(a4);
		
		return result;
	}
	
	
	
	
}
