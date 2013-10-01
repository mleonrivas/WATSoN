package com.want.core;

public class AgentRunner extends Thread {

	private AgentData agent;

	private boolean isFinished;

	private int indexAction;

	private Response response;

	private boolean doingPing;

	private Coordinator coordinator;

	public AgentRunner(AgentData a, Coordinator c) {
		doingPing = false;
		agent = a;
		isFinished = false;
		indexAction = 0;
		coordinator = c;
	}

	public void run() {

		while (!isFinished && !Thread.currentThread().isInterrupted()) {
			int counter = 0;
			response = null;
			Action action = agent.getPendingActions().get(indexAction);
			System.out.println("###$$$### action: " + action.getJSON());
			if (action.getAction().equals("wait")) {
				try {
					Thread.sleep(new Long(action.getData()));
				} catch (Exception e) {
					e.printStackTrace();
				}

			} else if (action.getAction().equals("waitUntil")) {
				//TODO implement wait until
				try {
				
					Thread.sleep(500);
				} catch (Exception e) {
					e.printStackTrace();
				}

			} else {
				agent.sendMsg(action.getJSON());
				while (response == null && counter < 10) {
					counter++;
					System.out.println(" %% WAITING " + counter + " %% "
							+ agent.getId());

					try {
						Thread.sleep(500);
					} catch (InterruptedException e) {
						e.printStackTrace();
						break;
					}
				}
				// System.out.println(" %% RESPONSE %% " + agent.getId() + " : "
				// + response.getAction());
				if (response != null && response.getAction().equals("exist")
						&& !response.getData().equals("true")) {
					indexAction--;
					try {
						Thread.sleep(1000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}

				if (response == null) {
					// start ping
					PingAgent ping = new PingAgent(agent, coordinator, this);
					ping.start();
					doingPing = true;
					while (doingPing == true) {
						try {
							Thread.sleep(500);
						} catch (InterruptedException e) {
							e.printStackTrace();
							Thread.currentThread().interrupt();
							indexAction = Integer.MAX_VALUE;
							break;
						}
					}
				}

			}
			indexAction++;
			isFinished = agent.getPendingActions().size() <= indexAction;
		}
	}

	/**
	 * @return the isFinished
	 */
	public final boolean isFinished() {
		return isFinished;
	}

	/**
	 * @param isFinished
	 *            the isFinished to set
	 */
	public final void setFinished(boolean isFinished) {
		this.isFinished = isFinished;
	}

	public final void notifyResponse(Response res) {
		System.out.println(" %% RESPONSE NOTIFIED %% " + agent.getId());
		response = res;
	}

	public Response getResponse() {
		return response;
	}

	public boolean isDoingPing() {
		return doingPing;
	}

	public void setDoingPing(boolean doingPing) {
		this.doingPing = doingPing;
	}

	public final void notifyEvent(Response res) {

	}

}
