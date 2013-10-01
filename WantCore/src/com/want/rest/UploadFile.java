package com.want.rest;

import java.io.IOException;

import org.restlet.representation.Representation;
import org.restlet.resource.Post;

public class UploadFile extends BaseResource{
//	
//	String URL;
//	
//	public void init(){
//		this.URL = (String) getRequestAttributes().get("URL");
//	}
//	
	@Post
	public String upload(Representation entity) throws IOException{
//		System.out.println("peticion OK");
//		HttpServletRequest httpRequest = ServletCall.getRequest(getRequest());
//		String res="";
		getCoordinator().getScripts().add(entity.getText());
		return entity.getText();

	}
	
}
