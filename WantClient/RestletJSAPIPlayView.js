var agentsConnected;
var host = "193.147.175.243";
var port = "8080";
function getAgentsPV(){
	var xmlHttp = null;
	xmlHttp = new XMLHttpRequest();
	xmlHttp.onreadystatechange=function(){
		    if (xmlHttp.readyState==4 && xmlHttp.status==200){
		      	}
	}
	xmlHttp.open( "GET", "http://" + host + ":" + port+ "/WantCore/agents", false );
	xmlHttp.send();
	drawContentPV(xmlHttp.responseText);
	return xmlHttp.responseText;
}
function getResponsesOfAgents(agent){
		var xmlHttp = null;
		xmlHttp = new XMLHttpRequest();
		xmlHttp.onreadystatechange=function(){
		    if (xmlHttp.readyState==4 && xmlHttp.status==200){
				cleanDivResponses();
				document.getElementById('responsesTextArea').innerHTML = xmlHttp.responseText;
		    }
		}
		xmlHttp.open( "GET", "http://" + host + ":" + port+ "/WantCore/responses/"+agent, true );
		xmlHttp.send();
		//drawResponsesAgent(xmlHttp);
		return xmlHttp;
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
		newButton.setAttribute('onclick','getResponsesOfAgents('+ '"' + agents[i] + '"' +')');
		
		toolbar.appendChild(newButton);
	}
}
function getServerLogs(){
	
	var xmlHttp = new XMLHttpRequest();
	xmlHttp.onreadystatechange=function(){
		if (xmlHttp.readyState==4 && xmlHttp.status==200){
			document.getElementById("textAreaLogs").innerHTML= xmlHttp.responseText;
		}
	}
	xmlHttp.open("GET","http://" + host + ":" + port+ "/WantCore/logs",true);
	xmlHttp.send();
}
function drawResponsesAgent(xmlHttp){
	cleanDivResponses();
}
function cleanDivResponses(){
	var div = document.getElementById('responses');
	var content = document.getElementById('contentPV');
	content.removeChild(div);
	var responsesdiv = document.createElement('div');
	responsesdiv.setAttribute('id',"responses");
	content.appendChild(responsesdiv);
	var textArea = document.createElement('textarea');
	textArea.setAttribute('id','responsesTextArea');
	responsesdiv.appendChild(textArea);
}