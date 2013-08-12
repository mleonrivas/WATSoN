package com.want.core;

import java.util.ArrayList;
import java.util.List;
import com.want.utils.Ping;

public class AgentImpl implements Agent{
	
	private String id;
	
	private String browser;
	
	private String browserVersion;
	
	private String oS;
	
	private List<String> pendingActions;
	
	private List<Response> responses;
	
	private boolean wait;
	
	public AgentImpl(){
		
	}
	public AgentImpl(String id,String browser,String browserVersion,String oS){
		this.id = id;
		this.browser = browser;
		this.browserVersion = browserVersion;
		this.oS = oS;
		pendingActions = new ArrayList<String>();
		responses = new ArrayList<Response>();
		wait = false;
	}
	
	@Override
	public String getId() {
		return id;
	}
	
	@Override
	public String getBrowser() {
		return browser;
	}
	
	@Override
	public String getBrowserVersion() {
		return browserVersion;
	}
	@Override
	public String getOS() {
		return oS;
	}
	@Override
	public void setPendingActions(List<String> pendingActions) {
		this.pendingActions = pendingActions;
	}
	@Override
	public String toString(){
		return "Agent: "+ id+ "\nbrowser: "+browser+"\nversion: "+browserVersion+"\nOS: "+oS;
	}
	
	@Override
	public List<String> getPendingActions() {
		return pendingActions;
	}
	@Override
	public void disconnect() {
		// TODO Auto-generated method stub	
	}
	@Override
	public boolean checkConnection() {
		return Ping.isAlive();
	}
	@Override
	public void addAction(String action) {
		pendingActions.add(action);
	}
	@Override
	public void sendMsg(String msg){
		Message message = new MessageImpl();
		message.sendMsg(msg, id);
	}
	@Override
	public void addResponse(Response response){
		responses.add(response);
	}
	@Override
	public List<Response> getReponses(){
		return responses;
	}
	@Override
	public boolean itsWait() {
		return wait;
	}
	@Override
	public void setItsWait(boolean wait) {
		this.wait = wait;
	}
}
