package com.want.core;

import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

import au.com.bytecode.opencsv.CSVReader;

import com.google.gson.Gson;
import com.want.utils.DomainActionsParserHelper;
import com.want.utils.Ping;

public class AgentDataImpl implements IAgentData{
	
	private String id;
	
	private String browser;
	
	private String browserVersion;
	
	private String oS;
	
	private List<Action> pendingActions;
	
	private List<Action> completedActions;
	
	private List<Response> responses;
	
	private boolean wait;
	
	private Integer indexOfScript;
	
	public AgentDataImpl(){
		
	}
	public AgentDataImpl(String id,String browser,String browserVersion,String oS){
		this.id = id;
		this.browser = browser;
		this.browserVersion = browserVersion;
		this.oS = oS;
		pendingActions = new ArrayList<Action>();
		completedActions = new ArrayList<Action>();
		responses = new ArrayList<Response>();
		wait = false;
		indexOfScript = 0;
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
	public String toString(){
		return "Agent: "+ id+ "\nbrowser: "+browser+"\nversion: "+browserVersion+"\nOS: "+oS;
	}
	
	@Override
	public List<Action> getPendingActions() {
		return pendingActions;
	}
	
	@Override
	public List<Action> getCompletedActions() {
		return completedActions;
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
	public void addAction(String actions) {
//		Gson gson= new Gson();
//		ActionSet set = gson.fromJson(actions, ActionSet.class);
//		for(int i=0; i<set.getActions().length; i++){
//			Action a = set.getActions()[i];
//			pendingActions.add(a);
//			//System.out.println(" %% %% ADD ACTION: %% %% " + a.getId() + ": " + a.getAction());
//		}
		IDomainActionsParser parser = DomainActionsParserHelper.getDomainActionsParser();
		ActionSet set = parser.parseDomainActions(actions);
		for(int i=0; i<set.getActions().length; i++){
			Action a = set.getActions()[i];
			pendingActions.add(a);
			//System.out.println(" %% %% ADD ACTION: %% %% " + a.getId() + ": " + a.getAction());
		}
		
		
		
	}
	@Override
	public void sendMsg(String msg){
		IMessage message = new MessageImpl();
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
//	@Override
//	public boolean itsWait() {
//		return wait;
//	}
//	@Override
//	public void setItsWait(boolean wait) {
//		this.wait = wait;
//	}
	
	
//  @Override
//	public Integer getIndexOfScript() {
//		return indexOfScript;
//	}
//	@Override
//	public void setIndexOfScript(Integer indexOfScript) {
//		this.indexOfScript = indexOfScript;
//	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
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
		AgentDataImpl other = (AgentDataImpl) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}
	
	
	
}
