var id;
var browser;
var browserVersion;
var oS;
var actionFinishedOK = true;

function readData(data) {
	var message = eval("(" + data + ");");
	window.x=message;
	var i=0;
	processListOfActions(message.actions,i);
}

function processListOfActions(list,i){
	if(i<list.length){
		processAction(list[i], function callbackFunction(){
			i = i+1;
			processListOfActions(list,i);
		});
	}else{
		//alert("fin");
	}
}

function processAction(action, callbackFunction){
	//alert(action.action);
	switch(action.action){
		case "click":
			sendLog(writeDebugLog("Agent " + id + " Received a \"click\" event: "+ action.id ));
			var res=false;
			var elementAux1 = document.evaluate(action.localParam,document,null,XPathResult.ORDERED_NODE_SNAPSHOT_TYPE,null);
			var element = elementAux1.snapshotItem(action.data);
			element.click();
			if(document.createEvent){
				res=true;
				actionFinishedOK = actionFinishedOK && res;
			}
			createResponse(action, res);
			callbackFunction.apply();
			break;
		case "focus":
			sendLog(writeDebugLog("Agent " + id + " Received a \"focus\" event: "+ action.id));
			var elementAux1 = document.evaluate(action.localParam,document,null,XPathResult.ORDERED_NODE_SNAPSHOT_TYPE,null);
			var element = elementAux1.snapshotItem(elementAux1.snapshotLength-1);
			element.focus();
			createResponse(action, false);
			callbackFunction.apply();
			break;
		case "exist":
			sendLog(writeDebugLog("Agent " + id + " Received a \"exist\" event: "+ action.id));
			var res=true;
			var count = 0;
			var elementAux1 = document.evaluate(action.localParam,document,null,XPathResult.ORDERED_NODE_SNAPSHOT_TYPE,null);
				var element = elementAux1.snapshotItem(action.data);
				if(element==null){
					while(element!=null){
						setinterval(count++,1000);
						if(count = 20){
							res = false;
							sendLog(writeDebugLog("Agent " + action.id + "not found."));
							break;
						}
					}
					//sendLog(writeDebugLog("Agent " + action.id + "not found."));
				}else{
					res=true;
					actionFinishedOK = actionFinishedOK && res;
				}
			createResponse(action, true);
			callbackFunction.apply();
			break;
		case "keypress":
			sendLog(writeDebugLog("Agent " + id + " Received a \"keypress\" event:"+ action.id));
			var elementAux1 = document.evaluate(action.localParam,document,null,XPathResult.ORDERED_NODE_SNAPSHOT_TYPE,null);
			var elementAux2 = elementAux1.sigleNodeValue;
			var element = elementAux2[elementAux2.length-1];
			if(element!=null){
				element.keypress();
			}
			createResponse(action, false);
			callbackFunction.apply();
			break;
		case "changeValue":
			sendLog(writeDebugLog("Agent " + id + " Received a \"changeValue\" event: "+ action.id));
			var res = false;
			var elementAux2 = document.evaluate(action.localParam,document,null,XPathResult.ORDERED_NODE_SNAPSHOT_TYPE,null);
			var textbox = elementAux2.snapshotItem(action.data);	
			textbox.value = action.configuration;	
			if (document.createEvent) {     
		        var changeEvent = document.createEvent ("HTMLEvents");
		        changeEvent.initEvent ("change", false, true);
		        textbox.dispatchEvent (changeEvent);
		        sendLog(writeDebugLog("[LOG textbox]Agent " + id + " value:" + textbox.value + " Text:" + textbox.Text));
		        res = true;
				actionFinishedOK = actionFinishedOK && res;
			}else{
				sendLog(writeDebugLog("[LOG textbox]Agent " + id + " " + textbox.value + " " + textbox.Text));
			}
			createResponse(action, res);
			callbackFunction.apply();
			break;
		case "wait":
			sendLog(writeDebugLog("Agent " + id + " Received a \"wait\" event: "+ action.id));
			setTimeout(callbackFunction, action.data);
			createResponse(action, true);
			callbackFunction.apply();
			break;
		case "end":
			sendLog(writeDebugLog("Agent " + id + " Received a \"end\" event: "+ action.id));
			setTimeout(callbackFunction, action.data);
			createResponse(action, actionFinishedOK);
			callbackFunction.apply();
			break;
		case "waitUntil":
			sendLog(writeDebugLog("Agent " + id + " Received a \"waitUntil\" event: "+ action.id));
			createResponse(action, true);
			callbackFunction.apply();
			break;
	}
}

function discharge(){
	 var BrowserDetect = { 
            init: function () { 
                this.browser = this.searchString(this.dataBrowser) || "An unknown browser"; 
                this.version = this.searchVersion(navigator.userAgent) || this.searchVersion(navigator.appVersion) || "an unknown version"; 
                this.OS = this.searchString(this.dataOS) || "an unknown OS"; 
            }, 
            searchString: function (data) { 
                for (var i=0;i<data.length;i++)    { 
                    var dataString = data[i].string; 
                    var dataProp = data[i].prop; 
                    this.versionSearchString = data[i].versionSearch || data[i].identity; 
                    if (dataString) { 
                        if (dataString.indexOf(data[i].subString) != -1) 
                            return data[i].identity; 
                    } 
                    else if (dataProp) 
                        return data[i].identity; 
                } 
            }, 
            searchVersion: function (dataString) { 
                var index = dataString.indexOf(this.versionSearchString); 
                if (index == -1) return; 
                return parseFloat(dataString.substring(index+this.versionSearchString.length+1)); 
            }, 
            dataBrowser: [ 
                           { string: navigator.userAgent, subString: "Chrome", identity: "Chrome" }, 
                           { string: navigator.userAgent, subString: "OmniWeb", versionSearch: "OmniWeb/", identity: "OmniWeb" }, 
                           { string: navigator.vendor, subString: "Apple", identity: "Safari" }, 
                           { prop: window.opera, identity: "Opera" }, 
                           { string: navigator.vendor, subString: "iCab", identity: "iCab" }, 
                           { string: navigator.vendor, subString: "KDE", identity: "Konqueror" }, 
                           { string: navigator.userAgent, subString: "Firefox", identity: "Firefox" }, 
                           { string: navigator.vendor, subString: "Camino", identity: "Camino" }, 
                           // for newer Netscapes (6+) 
                           { string: navigator.userAgent, subString: "Netscape", identity: "Netscape" }, 
                           { string: navigator.userAgent, subString: "MSIE", identity: "Explorer", versionSearch: "MSIE" }, 
                           { string: navigator.userAgent, subString: "Gecko", identity: "Mozilla", versionSearch: "rv" }, 
                           // for older Netscapes (4-) 
                           { string: navigator.userAgent, subString: "Mozilla", identity: "Netscape", versionSearch: "Mozilla" } 
                         ], 
             dataOS : [ 
                        { string: navigator.platform, subString: "Win", identity: "Windows" }, 
                        { string: navigator.platform, subString: "Mac", identity: "Mac" }, 
                        { string: navigator.platform, subString: "Linux", identity: "Linux" } 
                      ] };
	BrowserDetect.init();
	browser = BrowserDetect.browser;
	browserVersion = BrowserDetect.version;
	oS = BrowserDetect.OS;
	id = (Math.floor(Math.random() * (250+1)) + 1)+"-"+(Math.floor(Math.random() * (250+1)) + 1)+"-"+(Math.floor(Math.random() * (250+1)) + 1);
	var detectAgent = new Array();
	detectAgent[0]= id;
	detectAgent[1]= browser;
	detectAgent[2]= browserVersion;
	detectAgent[3]= oS;
	client.send('/queue/agents',{},detectAgent);
}

function sendLog(log){
	client.send('/queue/logs',{},log);
}

var ws = new SockJS('http://localhost:15674/stomp');
var client = Stomp.over(ws);

client.heartbeat.outgoing = 0;
client.heartbeat.incoming = 0;

var on_connect = function(x) {
	discharge();
	sendLog(writeDebugLog("Agent " + id + " Conected to server..."));
	idnt = client.subscribe('inputQueue',function(d) {
		var aux = d.body.split("X.");
		if(aux[0] == id){
			readData(aux[1]);
		}else{
			sendLog(writeDebugLog(id+": El msg no es mio"));
		}
    });
};
var on_error =  function() {
	sendLog(writeErrorLog("Agent" + id + "Connection error"));
};

client.connect('guest', 'guest', on_connect, on_error, '/');
$('#first input').focus(function() {
    if (!has_had_focus) {
        has_had_focus = true;
        $(this).val("");
    }
});

function createResponse(action,itsOkey){
    var response = "{response:[" +
    					"\n{" +
    						"\nid: "+ action.id + "," +
    						"\naction: "+ action.action + "," +
    						"\ndata: "+ itsOkey + "," +
    						"\nagent: "+ id +
    					"\n}]}";
    
    client.send('/queue/outputQueue',{},response);
    sendLog(writeDebugLog("Agent " + id + "reply: " + response));
}
