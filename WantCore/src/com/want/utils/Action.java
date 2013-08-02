package com.want.utils;

public class Action {
	private String name;
	private String action;
	public Action(String n, String a){
		name=n;
		action = a;
	}
	public String getName(){
		return name;
	}
	public String getAction(){
		return action;
	}
	public String toString(){
		return name +":\n "+action; 
	}
}
