package com.want.domains;

import com.google.gson.Gson;
import com.want.core.ActionSet;
import com.want.core.IDomainActionsParser;

public class DefaultActionsParser implements IDomainActionsParser {

	@Override
	public ActionSet parseDomainActions(String actions) {
		Gson gson= new Gson();
 		ActionSet set = gson.fromJson(actions, ActionSet.class);
		return set;
	}

}
