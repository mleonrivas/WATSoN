package com.want.domains.ice.actions;

import java.util.LinkedList;
import java.util.List;

import com.want.core.Action;
import com.want.utils.ConfigurationProperties;
import com.want.utils.DefaultProperties;

public class Login implements IDomainAction {
	
	
	
	public Login(){
		
	}
	
	@Override
	public List<Action> getAgentActions(String id, String action, String param, String name,
			String data, String lastAction) {
		List<Action> result = new LinkedList<Action>();
	
			String[] in = param.split("/", 2);
			
			Action a3 = new Action();
			a3.setId(id + ".1");
			a3.setAction("changeValue");
			a3.setLocalizator("xpath");
			a3.setLocalParam("//*[@class=\"loginFormContainer\"]/*[@class=\"gwt-TextBox\"]");
			a3.setConfiguration(in[0]);
			a3.setData("0");
			
			Action a4 = new Action();
			a4.setId(id + ".2");
			a4.setAction("changeValue");
			a4.setLocalizator("xpath");
			a4.setLocalParam("//*[@class=\"loginFormContainer\"]/*[@class=\"gwt-PasswordTextBox\"]");
			a4.setConfiguration(in[1]);
			a4.setData("0");
			
			result.add(a3);
			result.add(a4);
			
			Action a1 = new Action();
			a1.setId(id + ".3");
			a1.setAction("click");
			a1.setLocalizator("xpath");
			a1.setLocalParam("//*[@class=\"gwt-Button\"][.=\"Login\"]");
			a1.setConfiguration("");
			a1.setData("0");
			
			Action a2 = new Action();
			a2.setId(id + ".4");
			a2.setAction("wait");
			a2.setLocalizator("");
			a2.setLocalParam("");
			a2.setConfiguration("");
			a2.setData(ConfigurationProperties.getInstance().getProperties().getProperty("wait_time_after_click_button", DefaultProperties.DEFAULT_WAIT_TIME_AFTER_CLICK_BUTTON));
			result.add(a1);
			result.add(a2);
			
			
			return result;
	}
}
