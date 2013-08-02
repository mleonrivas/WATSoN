package com.want.rest;

import java.io.BufferedReader;
import java.io.IOException;

import javax.servlet.http.HttpServletRequest;

import org.restlet.ext.servlet.internal.ServletCall;
import org.restlet.representation.Representation;
import org.restlet.resource.Post;

import com.want.utils.Action;

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
