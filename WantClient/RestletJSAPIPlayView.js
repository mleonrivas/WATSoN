var agentsConnected;
function getAgentsPV(){
	var xmlHttp = null;
	xmlHttp = new XMLHttpRequest();
	xmlHttp.onreadystatechange=function(){
		    if (xmlHttp.readyState==4 && xmlHttp.status==200){
		      	}
	}
	xmlHttp.open( "GET", "http://localhost:8080/WantCore/agents", false );
	xmlHttp.send();
	drawContentPV(xmlHttp.responseText);
	return xmlHttp.responseText;
}
function getResponsesOfAgents(agent){
		var xmlHttp = null;
		xmlHttp = new XMLHttpRequest();
		xmlHttp.open( "GET", "http://localhost:8080/WantCore/responses/"+agent, true );
		//xmlHttp.send();
		return xmlHttp.responseText;
}
function drawContentPV(xmlHttp){
	var agents = xmlHttp.split("\n");
	agentsConnected = agents;
	var head = document.getElementById('headPV');
	var toolbar = document.createElement('div');
	toolbar.setAttribute('id','toolbar');
	head.appendChild(toolbar);
	var content = document.getElementById('contentPV');
	var responses = document.createElement('div');
	responses.setAttribute('id',"responses");
	content.appendChild(responses);

	for(var i = 0; i<agents.length-1;i++){
		var newButton = document.createElement('button');
		newButton.innerHTML = agents[i];
		newButton.setAttribute('id',agents[i]);
		newButton.setAttribute('onclick','drawResponsesAgent('+ '"' + agents[i] + '"' +')');
		toolbar.appendChild(newButton);
	}
}
function drawResponsesAgent(agent){
	var responses = getResponsesOfAgents(agent);
	cleanDivResponses();
	//var label = document.getElementById('responsesLabel').innerHTML = responses;
	document.getElementById('responsesLabel').innerHTML = responses;
	//label.innerHTML = responses;
	
}
function cleanDivResponses(){
	var div = document.getElementById('responses');
	var content = document.getElementById('contentPV');
	content.removeChild(div);
	var responsesdiv = document.createElement('div');
	responsesdiv.setAttribute('id',"responses");
	content.appendChild(responsesdiv);
	var label = document.createElement('label');
	label.setAttribute('id','responsesLabel');
	responsesdiv.appendChild(label);
}