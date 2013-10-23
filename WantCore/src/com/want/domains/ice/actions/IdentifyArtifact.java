package com.want.domains.ice.actions;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import com.want.core.Action;
import com.want.utils.ConfigurationProperties;

public class IdentifyArtifact implements IDomainAction {
	
	private Map<String, String> tabs;
	
	public IdentifyArtifact(){
		tabs = new HashMap<String,String>();
		tabs.put("File Upload", "0");
		tabs.put("Existing Artifact", "1");
		tabs.put("External URL", "2");
		tabs.put("Secure Input", "3");
	}
	
	@Override
	public List<Action> getAgentActions(String id, String action, String param, String name,
			String data, String lastAction) {
		List<Action> result = new LinkedList<Action>();
		Action a1 = new Action();
		a1.setId(id + ".1");
		a1.setAction("click");
		a1.setLocalizator("xpath");
		a1.setLocalParam("//*[@class = \"tab\"]");
		a1.setConfiguration("");
		a1.setData(tabs.get(param.trim()));
		
		Action a2 = new Action();
		a2.setId(id + ".2");
		a2.setAction("wait");
		a2.setLocalizator("");
		a2.setLocalParam("");
		a2.setConfiguration("");
		a2.setData(ConfigurationProperties.getInstance().getProperties().getProperty("wait_time_after_click_tab", "2000"));
		
		result.add(a1);
		result.add(a2);
		
		if(param.trim().equals("Existing Artifact")){
			Action a3 = new Action();
			a3.setId(id + ".3");
			a3.setAction("click");
			a3.setLocalizator("xpath");
			a3.setLocalParam("//*[@class=\"ResultArtifactLine\"][.=\"" + name + "\"]");
			a3.setConfiguration("");
			a3.setData("0");
			result.add(a3);
		}else if(param.trim().equals("External URL")){
			Action a3 = new Action();
			a3.setId(id + ".3");
			a3.setAction("changeValue");
			a3.setLocalizator("xpath");
			a3.setLocalParam("//*[@class=\"KVPropLine\"]/div[.=\"External URL:\"]/../input");
			a3.setConfiguration(data);
			a3.setData("0");
			
			Action a4 = new Action();
			a4.setId(id + ".4");
			a4.setAction("changeValue");
			a4.setLocalizator("xpath");
			a4.setLocalParam("//*[@class=\"KVPropLine\"]/div[.=\"Artifact Name:\"]/../input");
			a4.setConfiguration(name);
			a4.setData("0");
			
			result.add(a3);
			result.add(a4);
		}else if(param.trim().equals("Secure Input")){
			
			Action a3 = new Action();
			a3.setId(id + ".3");
			a3.setAction("click");
			a3.setLocalizator("xpath");
			a3.setLocalParam("//*[@class=\"gwt-Button\"][.=\"Enter Secure Content\"]");
			a3.setConfiguration("");
			a3.setData("0");
			
			Action a4 = new Action();
			a4.setId(id + ".4");
			a4.setAction("wait");
			a4.setLocalizator("");
			a4.setLocalParam("");
			a4.setConfiguration("");
			a4.setData(ConfigurationProperties.getInstance().getProperties().getProperty("wait_time_after_click_tab", "2000"));
			
			Action a5 = new Action();
			a5.setId(id + ".5");
			a5.setAction("changeValue");
			a5.setLocalizator("xpath");
			a5.setLocalParam("//*[@class=\"gwt-TextArea\"]");
			a5.setConfiguration(data);
			a5.setData("0");
			
			
			Action a6 = new Action();
			a6.setId(id + ".6");
			a6.setAction("click");
			a6.setLocalizator("xpath");
			a6.setLocalParam("//*[@class=\"gwt-Button\"][.=\"Submit Secure Hash\"]");
			a6.setConfiguration("");
			a6.setData("0");
			
			Action a7 = new Action();
			a7.setId(id + ".7");
			a7.setAction("wait");
			a7.setLocalizator("");
			a7.setLocalParam("");
			a7.setConfiguration("");
			a7.setData(ConfigurationProperties.getInstance().getProperties().getProperty("wait_time_after_click_tab", "2000"));
			
			result.add(a3);
			result.add(a4);
			result.add(a5);
			result.add(a6);
			result.add(a7);
		}
		
		
		return result;
	}


}
