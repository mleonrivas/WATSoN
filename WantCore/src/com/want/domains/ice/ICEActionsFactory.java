package com.want.domains.ice;

import com.want.domains.ice.actions.CancelTask;
import com.want.domains.ice.actions.ConfirmTask;
import com.want.domains.ice.actions.IDomainAction;
import com.want.domains.ice.actions.IdentifyArtifact;
import com.want.domains.ice.actions.IdentifyFragment;
import com.want.domains.ice.actions.InsertAsset;
import com.want.domains.ice.actions.OpenTask;
import com.want.domains.ice.actions.SelectOption;
import com.want.domains.ice.actions.SubmitTask;

public class ICEActionsFactory {
	
	private CancelTask cancelTask;
	private ConfirmTask confirmTask;
	private IdentifyArtifact identifyArtifact;
	private IdentifyFragment identifyFragment;
	private InsertAsset insertAsset;
	private OpenTask openTask;
	private SelectOption selectOption;
	private SubmitTask submitTask;
	
	public ICEActionsFactory(){
		cancelTask = new CancelTask();
		confirmTask = new ConfirmTask();
		identifyArtifact = new IdentifyArtifact();
		identifyFragment = new IdentifyFragment();
		insertAsset = new InsertAsset();
		openTask = new OpenTask();
		selectOption = new SelectOption();
		submitTask = new SubmitTask();
		
	}
	
	public IDomainAction getAction(String action){
		String a = action.trim();
		if(a.equalsIgnoreCase("Cancel Task")){
			return cancelTask;
		}else if(a.equalsIgnoreCase("Confirm Task")){
			return confirmTask;
		}else if(a.equalsIgnoreCase("Identify Artifact")){
			return identifyArtifact;
		}else if(a.equalsIgnoreCase("Identify Procedure")){
			return identifyArtifact;
		}else if(a.equalsIgnoreCase("Identify Fragment")){
			return identifyFragment;
		}else if(a.equalsIgnoreCase("Insert Asset")){
			return insertAsset;
		}else if(a.equalsIgnoreCase("Open Task")){
			return openTask;
		}else if(a.equalsIgnoreCase("Submit Task")){
			return submitTask;
		}else if(a.equalsIgnoreCase("Select Option")){
			return selectOption;
		}
		
		return null;
	}
}
