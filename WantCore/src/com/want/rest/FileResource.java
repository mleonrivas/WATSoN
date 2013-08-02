package com.want.rest;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import org.restlet.data.MediaType;
import org.restlet.representation.Representation;
import org.restlet.resource.Get;
import org.restlet.resource.Put;

import com.want.utils.Action;
import com.want.utils.FileImporter;


public class FileResource extends BaseResource{
	
	@Get
	public String actions(){
//		
//		File dir = new File("WEB-INF/scripts");
//		for(int i = 0;i<dir.list().length;i++){
//			String file=dir.list()[i];
//			FileImporter fi = new FileImporter(file);
//			Action action = new Action(fi.getName(),fi.getContent());
//			getCoordinator().getScripts().add(action);
//		}
//
//		
		String res = "";
		for(String s:getCoordinator().getScripts()){
//			res = res +" "+ a.getName()+" "+ a.getAction()+"\n";
			res = res +"\n-----------------\n "+ s;
		}
		return res;
		
	}

}
