package com.want.core;

import java.util.List;

/**
 * @author  Gabriel
 */
public interface Agent {
	/**
	 * @uml.property  name="id"
	 */
	String getId();
	/**
	 * @uml.property  name="browser"
	 */
	String getBrowser();
	/**
	 * @uml.property  name="browserVersion"
	 */
	String getBrowserVersion();
	/**
	 * @uml.property  name="oS"
	 */
	String getOS();
	String toString();
	List<String> getPendingActions();
	void disconnect();
	boolean checkConnection();
	void addAction(String action);
	void setPendingActions(List<String> actions);
	void sendMsg(String msg);
	void addResponse(Response response);
	List<Response> getReponses();
	boolean itsWait();
	void setItsWait(boolean wait);
}
