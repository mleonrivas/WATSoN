package com.want.domains.ice.actions;

import java.util.List;

import com.want.core.Action;

public interface IDomainAction {
	
	List<Action> getAgentActions(String id, String action, String param, String name, String data, String lastAction);
	
}
