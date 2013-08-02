		function getAgents(){
			var xmlHttp = null;
		    xmlHttp = new XMLHttpRequest();
		    xmlHttp.onreadystatechange=function(){
		    	if (xmlHttp.readyState==4 && xmlHttp.status==200){
		     		alert(xmlHttp.responseText);
		     		
		      	}
		    }
		    xmlHttp.open( "GET", "http://localhost:8080/WantCore/agents", true );
		    xmlHttp.send();
		    return xmlHttp.responseText;
		}
		function getScripts(){
			var xmlHttp = null;
		    xmlHttp = new XMLHttpRequest();
		    xmlHttp.open( "GET", "http://localhost:8080/WantCore/files", true );
		    xmlHttp.send();
		    return xmlHttp.responseText;
		}
		function uploadFile(){
			var fileInput = document.getElementById("file");
			function handleFileSelect(evt) {
			    var files = evt.target.files; // FileList object
			
			    // files is a FileList of File objects. List some properties.
			    for (var i = 0, f; f = files[i]; i++) {
					
					var reader = new FileReader();
					reader.onload = (function(theFile) {
				        return function(e) {
				        	var xmlHttp = null;
						    xmlHttp = new XMLHttpRequest();
						    xmlHttp.open( "POST", "http://localhost:8080/WantCore/fileUpload/", true );
						    xmlHttp.send(theFile);
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
		    xmlHttp.open( "GET", "http://localhost:8080/WantCore/play", true );
		    xmlHttp.send();
		}
		function getResponsesOfAegents(){
			var xmlHttp = null;
		    xmlHttp = new XMLHttpRequest();
		    xmlHttp.onreadystatechange=function(){
		    	if (xmlHttp.readyState==4 && xmlHttp.status==200){
		     		alert(xmlHttp.responseText);
		      	}
		    }
		    xmlHttp.open( "GET", "http://localhost:8080/WantCore/responses", true );
		    xmlHttp.send();
		}
		function dispenseTask(action,agent){
			var xmlHttp = null;
		    xmlHttp = new XMLHttpRequest();
		    xmlHttp.open( "PUT", "http://localhost:8080/WantCore/add/"+action+"/"+agent, true );
		    xmlHttp.send();
		}
		function getDispensedTasks(){
			var xmlHttp = null;
		    xmlHttp = new XMLHttpRequest();
		    xmlHttp.open( "GET", "http://localhost:8080/WantCore/add/"+action+"/"+agent, true );
		    xmlHttp.send();
		}