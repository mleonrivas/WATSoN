		var host = "193.147.175.243";
		var port = "8080";
		function getAgents(){
			var xmlHttp = null;
		    xmlHttp = new XMLHttpRequest();
		     xmlHttp.onreadystatechange=function(){
		    	if (xmlHttp.readyState==4 && xmlHttp.status==200){
		      	 }
		    }
		    xmlHttp.open( "GET", "http://" + host + ":" + port+ "/WantCore/rest/agents", false );
			xmlHttp.send();
			drawContent(xmlHttp.responseText);
			return xmlHttp.responseText;
		}
		function getScripts(){
			var xmlHttp = null;
		    xmlHttp = new XMLHttpRequest();
		    xmlHttp.open( "GET", "http://" + host + ":" + port+ "/WantCore/rest/files", true );
		    xmlHttp.send();
		    return xmlHttp.responseText;
		}
		function prepareUpload(){
			var nodesSnapshot = document.evaluate('//*[@id = "select"]',document,null,XPathResult.ORDERED_NODE_SNAPSHOT_TYPE,null);
			var nodesSnapshotAgent = document.evaluate('//*[@id2 = "labelAgent"]',document,null,XPathResult.ORDERED_NODE_SNAPSHOT_TYPE,null);
			for ( var i=0 ; i < nodesSnapshot.snapshotLength; i++ ){
				var fileInput = nodesSnapshot.snapshotItem(i);
				var agent = nodesSnapshotAgent.snapshotItem(i);
				uploadFile(fileInput,agent.textContent);
				
			}	
		}
		function uploadFile(fileInput, agent){
			//var fileInput = document.getElementById("file");
			function handleFileSelect(evt) {
			    var files = evt.target.files; // FileList object			
			    // files is a FileList of File objects. List some properties.
			    for (var i = 0, f; f = files[i]; i++) {					
					var reader = new FileReader();
					reader.onload = (function(theFile) {
				        return function(e) {
				        	var xmlHttp = null;
						    xmlHttp = new XMLHttpRequest();
						    xmlHttp.open( "POST", "http://" + host + ":" + port+ "/WantCore/rest/fileUpload/", true );
						    xmlHttp.send(theFile);
							dispenseTask(theFile,agent);
							//dispenseTask(reader.readAsText(f),agent);
				        };
				      })(f);
				    
				    reader.readAsText(f);
			    }
			  }
			  fileInput.addEventListener('change', handleFileSelect, false);			
		}
		function play(){
			var xmlHttp = null;
		    xmlHttp = new XMLHttpRequest();
		    xmlHttp.open( "GET", "http://" + host + ":" + port+ "/WantCore/rest/play", true );
		    xmlHttp.send();
		}
		function removeScript(action){
			var xmlHttp = null;
		    xmlHttp = new XMLHttpRequest();
			xmlHttp.open( "DELETE", "http://" + host + ":" + port+ "/WantCore/rest/add/"+agent, true );
		    xmlHttp.send(action);
		}
		function dispenseTask(action,agent){
			var xmlHttp = null;
		    xmlHttp = new XMLHttpRequest();
			xmlHttp.open( "PUT", "http://" + host + ":" + port+ "/WantCore/rest/add/"+agent, true );
		    xmlHttp.send(action);
			addImg(agent, action);
		}
		function addImg(agent,action){
			var nodesSnapshotDiv = document.evaluate('//*[@id2 = "'+agent+'"]',document,null,XPathResult.ORDERED_NODE_SNAPSHOT_TYPE,null);
			var div = nodesSnapshotDiv.snapshotItem(0);
			var img = document.createElement('img');
			img.setAttribute('src','styles/images/file.jpg');
			img.setAttribute('id','img');
			img.setAttribute('onclick','removeScript('+'"'+ action +'"'+')');
			div.appendChild(img);
		}
		function getDispensedTasks(){
			var xmlHttp = null;
		    xmlHttp = new XMLHttpRequest();
		    xmlHttp.open( "GET", "http://" + host + ":" + port+ "/WantCore/rest/add/"+agent, true );
		    xmlHttp.send();
		}
		function drawContent(xmlHttp){
		var agents = xmlHttp.split("\n");
			for(var i = 0; i<agents.length-1;i++){
				var div = document.getElementById('content');
				var newdiv = document.createElement('div');
				newdiv.setAttribute('id',"agent");
				newdiv.setAttribute('id2', agents[i]);
				div.appendChild(newdiv);

				var newLabel = document.createElement('label');
				newLabel.innerHTML = agents[i];
				newLabel.setAttribute('id', "labelAgent"+i);
				newLabel.setAttribute('id2', "labelAgent");
			
				//var newTextBox = document.createElement('input');
				//newTextBox.id = "textAgent"+i;
				
				var newInput = document.createElement('input');
				newInput.setAttribute('type', "file");
				newInput.setAttribute('id',"select");

				newdiv.appendChild(newLabel);
				//newdiv.appendChild(newTextBox);
				newdiv.appendChild(newInput);
			}		
		}
		
