package com.want.rest;

import org.restlet.resource.Post;

public class StopCore extends BaseResource{
	@Post
	public void stop(){
		getCoordinator().stop();
	}
}
