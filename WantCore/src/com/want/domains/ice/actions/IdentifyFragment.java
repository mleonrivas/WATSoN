package com.want.domains.ice.actions;

import java.util.LinkedList;
import java.util.List;

import com.want.core.Action;

public class IdentifyFragment implements IDomainAction {

	@Override
	public List<Action> getAgentActions(String id, String action, String param, String name,
			String data, String lastAction) {
		List<Action> result = new LinkedList<Action>();
		Action a1 = new Action();
		a1.setId(id + ".1");
		a1.setAction("changeInnerHTML");
		a1.setLocalizator("nestedXpath");
		a1.setLocalParam("//*[@class = \"gwt-RichTextArea editing\"];//body");
		a1.setConfiguration(data);
		a1.setData("0");
		
		result.add(a1);
		
		return result;
		
	}
//
//	{
//		id:"19.6",
//		action: "changeInnerHTML",
//		localizator: "nestedXpath",
//		localParam: '//*[@class = "gwt-RichTextArea editing"];//body',
//		configuration: "MailServer: <br /> - IMAP: Protocol used for accessing mails, allowed only through SSL/TLS.",
//		data: "0"	
//	},
	
}
