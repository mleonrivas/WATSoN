package com.want.core;

public class Action {
	
	private String id;
	
	private String action;
	
	private String localizator;
	
	private String localParam;
	
	private String configuration;
	
	private String data;

	public Action (){
		
	}
	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getAction() {
		return action;
	}

	public void setAction(String action) {
		this.action = action;
	}

	public String getLocalizator() {
		return localizator;
	}

	public void setLocalizator(String localizator) {
		this.localizator = localizator;
	}

	public String getLocalParam() {
		return localParam;
	}

	public void setLocalParam(String localParam) {
		this.localParam = localParam;
	}

	public String getConfiguration() {
		return configuration;
	}

	public void setConfiguration(String configuration) {
		this.configuration = configuration;
	}

	public String getData() {
		return data;
	}

	public void setData(String data) {
		this.data = data;
	}
	
	public String getJSON(){
		String config = configuration;
		if(!configuration.trim().startsWith("{") && !configuration.trim().endsWith("}")){
			config = "\"" + configuration + "\"";
		}
		String result = "{ actions:[{";
		result += "id:\"" + id + "\", ";
		result += "action:\"" + action + "\", ";
		result += "localizator:\"" + localizator + "\", ";
		result += "localParam: '" + localParam + "', ";
		result += "configuration:" + config + ", ";
		result += "data:\"" + data + "\"";
		result += "}]}";
		return result;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((this.getJSON() == null) ? 0 : this.getJSON().hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Action other = (Action) obj;
		if (!this.getJSON().equals(other.getJSON()))
			return false;
		return true;
	}
	
	
	
	
}
