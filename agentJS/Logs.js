var debugHead = "[INFO]";
var warnHead = "[WARN]";
var errorHead = "[ERROR]";

function writeDebugLog(log){
	var res = debugHead + " " + log;
	return res;
}
function writeWarningLog(log){
	var res = warnHead + " " + log;
	return res;
}
function writeErrorLog(log){
	var res = errorHead + " " + log;
	return res;
}
