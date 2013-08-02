package com.want.rest;

import org.restlet.resource.Get;


public class Play extends BaseResource{


	@Get
	public void getContent(){
		getCoordinator().play();
	}
	

}

