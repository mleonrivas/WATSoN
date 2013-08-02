package com.want.factory;

import com.want.core.Response;

public class ResponsesFactory {
	public static Response responseOfAgent(String message){
		String response = message.replace("{", "");
		response = message.replace("[", "");
		response = message.replaceAll("response: ", "");
		response = message.replaceAll(" ", "");
		String[] responseArray = response.split(",");
		String id = responseArray[0].split(":")[1];
		String action = responseArray[1].split(":")[1];
		String data = responseArray[2].split(":")[1];
		String agent = responseArray[3].split(":")[1].replace("}", "").replace("]", "").replace("\n", "");
		Response res = new Response(id, action, data, agent);
		return res;
	}
}
