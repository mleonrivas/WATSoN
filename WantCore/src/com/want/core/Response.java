package com.want.core;

public class Response {
	
	/**
	 * @uml.property  name="id"
	 */
	private String id;
	/**
	 * @uml.property  name="data"
	 */
	private String data;
	/**
	 * @uml.property  name="action"
	 */
	private String action;
	/**
	 * @uml.property  name="agent"
	 */
	private String agent;
	
	public Response(String id, String action,String data,String agent){
		this.id = id;
		this.action = action;
		this.agent = agent;
		this.data = data;
	}

	/**
	 * @return
	 * @uml.property  name="id"
	 */
	public String getId() {
		return id;
	}

	/**
	 * @param id
	 * @uml.property  name="id"
	 */
	public void setId(String id) {
		this.id = id;
	}

	/**
	 * @return
	 * @uml.property  name="data"
	 */
	public String getData() {
		return data;
	}

	/**
	 * @param data
	 * @uml.property  name="data"
	 */
	public void setData(String data) {
		this.data = data;
	}

	/**
	 * @return
	 * @uml.property  name="action"
	 */
	public String getAction() {
		return action;
	}

	/**
	 * @param action
	 * @uml.property  name="action"
	 */
	public void setAction(String action) {
		this.action = action;
	}

	/**
	 * @return
	 * @uml.property  name="agent"
	 */
	public String getAgent() {
		return agent;
	}

	/**
	 * @param agent
	 * @uml.property  name="agent"
	 */
	public void setAgent(String agent) {
		this.agent = agent;
	}
	
	public String toString(){
		return "Response: " + getId() + ", " + getAction() + ", " +getData() + ", " +getAgent();
	}
	
}
