package com.want.rest;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import org.restlet.data.MediaType;
import org.restlet.representation.Representation;
import org.restlet.resource.Delete;
import org.restlet.resource.Get;
import org.restlet.resource.Put;

import com.want.core.Agent;
import com.want.utils.Action;
import com.want.utils.FileImporter;


public class FileResource extends BaseResource{
	
	
	@Get
	public String actions(){
	
		String res = "";
		for(Agent a : getCoordinator().getAgentsConnected()){
			for(String s:a.getPendingActions()){
				res = res +"\n-----------------\n "+ s;
			}
		}
		return res;
	}

}
