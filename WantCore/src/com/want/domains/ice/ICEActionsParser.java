package com.want.domains.ice;

import java.io.IOException;
import java.io.StringReader;
import java.util.LinkedList;
import java.util.List;

import au.com.bytecode.opencsv.CSVReader;

import com.want.core.Action;
import com.want.core.ActionSet;
import com.want.core.IDomainActionsParser;
import com.want.domains.ice.actions.IDomainAction;

public class ICEActionsParser implements IDomainActionsParser {

	@Override
	public ActionSet parseDomainActions(String actions) {
		ActionSet result = new ActionSet();
		List<Action> actionList = new LinkedList<Action>();
		try {
			ICEActionsFactory factory = new ICEActionsFactory();
			CSVReader reader = new CSVReader(new StringReader(actions));
			String[] line = reader.readNext();
			String last = null;
			while(line!=null){
				IDomainAction da = factory.getAction(line[1]);
				System.out.println("IMPORTING LINE: " + line[0] + " $, " + line[1] + " $, " + line[2] + " $, " + line[3] + " $, " + line[4].replace("\n", "<br />"));
				
				actionList.addAll(da.getAgentActions(line[0], line[1], line[2], line[3], line[4], last));
				last = line[1];
				line = reader.readNext();
			}
			
			
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		Action[] array = new Action[actionList.size()];
		result.setActions(actionList.toArray(array));
		return result;
	}
	
}
