package com.want.core;

import java.util.List;

/**
 * @author  Gabriel
 */
public interface IAgentData {
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
	List<Action> getPendingActions();
	List<Action> getCompletedActions();
	void disconnect();
	boolean checkConnection();
	void addAction(String action);
	void sendMsg(String msg);
	void addResponse(Response response);
	List<Response> getReponses();
	//boolean itsWait();
	//void setItsWait(boolean wait);
	//Integer getIndexOfScript();
	//void setIndexOfScript(Integer indexOfScript);
}
