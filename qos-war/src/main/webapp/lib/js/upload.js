$(function(){
	
	$("#uploadBtn").on( "click", function() {
		console.log('Start uploadBtn');
		  
		measureUpload();
	});
});

//A function to process messages received by the window.
function receiveMessage(e) {
	console.log('Upload.js receiveMessage');
	var target_origin = e.data['target_origin'];
	var task = e.data['task'];
	// Check to make sure that this message came from the correct domain.
	//if (target_origin.indexOf(e.origin))
	//	return;

	switch ( task ) { // postMessage tasks
        // display height received in postMessage
        case 'upload' :
        	serverUrl = target_origin;
            messageEle.innerHTML = "Target origin: " + target_origin;
            // start measure upload
            measureUpload();
            break;

        case 'clear' :
            // nothing to do for this one
            break;
        
        //default:
    }
}

var windowProxy;
window.onload = function() {
	console.log('Upload.js onload');
	windowProxy = new Porthole.WindowProxy("http://118.71.224.225:8080/qos/proxy.html"); 
	windowProxy.addEventListener(receiveMessage);
	
	// Get a reference to the div on the page that will display the
	// message text.
	messageEle = document.getElementById('message');

	// Setup an event listener that calls receiveMessage() when the window
	// receives a new MessageEvent.
//	window.addEventListener('message', receiveMessage);
}
var messageEle;
//---------------------- Upload ---------------------
// Interval
var intervalUpload;
var intervalUpdateResultToParent;

var serverUrl;
var start;
var end;
var uploadingSpeed;
var uploadProgressPercent;
var uploadEvtLoaded;
var uploadEvtTotal;
var durationUpload;
var startUploadTime;
var progressUploadTime;
var endUploadTime;
//var isBusy = false;
var unitType = 1;
//var serverUrl = 'http://localhost:8080/qos';
var xhr;

var data = [];

function measureUpload() {
	$('#uploadStatus').html('UPLOADING');
	
	// Init data
	start = new Date().getTime();
	data = [];
	
	intervalUpload = setInterval(uploadFile, UPLOAD_TIME_SLEEP);
	intervalUpdateResultToParent = setInterval(updateResultToParent, UPLOAD_TIME_SLEEP);

	// Check upload time to terminate
	setTimeout(terminateUpload, DURATION_UPLOAD + 100);
}

function updateResultToParent() {
	// Get data from input
	var currentTime = new Date().getTime();
	var result = $('#uploadingSpeed').val();
	var status = $('#uploadStatus').html();
	if (result == undefined || result == "") {
		return;
	}
	// push into array
	data.push(result);

	// Post msg
	if (status == 'COMPLETED') {
		//parent.postMessage( {'task': 'completed', 'data': result, 'time': currentTime}, serverUrl );
		windowProxy.post({'task': 'completed', 'data': result, 'time': currentTime});
	} else {
//		parent.postMessage( {'task': 'uploading', 'data': result, 'time': currentTime}, serverUrl );
		windowProxy.post({'task': 'uploading', 'data': result, 'time': currentTime});
	}
}

function terminateUpload() {
	console.log('terminateUpload');
	
	end = new Date().getTime();
	console.log('terminateUpload after time:' + (end - start));
	
	clearInterval(intervalUpload);
	clearInterval(intervalUpdateResultToParent);
	// Abort uploading
	
	console.log('-----abort xhr-----');
	if (xhr && xhr.readyState != 4) {
		xhr.abort();
	}
	
	$('#uploadStatus').html('COMPLETED');
	console.log(data);
}

function randomString(len) {
    var charSet = 'ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789';
    var randomString = '';
    for (var i = 0; i < len; i++) {
    	var randomPoz = Math.floor(Math.random() * charSet.length);
    	randomString += charSet.substring(randomPoz,randomPoz+1);
    }
    return randomString;
}

function uploadFile() {
    var fd = new FormData();
    
    // 1024k
    var uploadData = randomString(1024 * UPLOAD_SIZE_SAMPLE);
	var filename = "upload" + (new Date).getTime() + ".bin";
    var file = new File([uploadData], filename);
    fd.append("file", file)
    
    xhr = $.ajax({
	    xhr: function() {
	        var xhr = new window.XMLHttpRequest();
//	        xhr.upload.addEventListener("loadstart", uploadStart, false);
	        xhr.upload.addEventListener("progress", uploadProgress, false);
//	        xhr.upload.addEventListener("load", uploadComplete, false);
//	        xhr.upload.addEventListener("error", uploadFailed, false);
//	        xhr.upload.addEventListener("abort", uploadCanceled, false);
	
	       return xhr;
	    },
	    //crossDomain : true,
	    //dataType: 'jsonp',
	    //iframe: true,
	    cache: false,
        contentType: false,
        processData: false,
	    type: 'POST',
	    url: serverUrl + "/api/file/upload",
	    data: fd,
	    success: function(data){
	        //Do something on success
	        console.log('upload done');
	    },
	    xhrFields: {
	        //withCredentials: true,
	    },
	    beforeSend: function(xmlHttpRequest) {
//	    	xmlHttpRequest.withCredentials = true;
            startUploadTime = new Date().getTime();
        }
	});
}

function uploadProgress(evt) {
    if (evt.lengthComputable) {
        uploadProgressPercent = Math.round(evt.loaded * 100 / evt.total)
        progressUploadTime = (new Date()).getTime();
        
        var loaded = evt.loaded;
        var durationUpload = (progressUploadTime - startUploadTime);
        console.log('------------ uploadProgress: ' + uploadProgressPercent);
        console.log('------------ loaded:' + loaded);
        console.log('------------ durationUpload:' + durationUpload);
        
        if (durationUpload == 0 || loaded == 0) {
        	return;
        }
        // Calculate
        uploadingSpeed = calculateBandwidth(loaded, durationUpload, 1);
        $('#uploadingSpeed').val(uploadingSpeed);
    } else {
        uploadProgressPercent = 'unable to compute';
        console.log('unable to compute');
    }
}

function uploadStart(evt) {
    startUploadTime = (new Date()).getTime();
	console.log('--- Start Upload file take time --');
}

function uploadComplete(evt) {
    /* This event is raised when the server send back a response */
    //alert(evt.target.responseText)
	endUploadTime = (new Date()).getTime();
	durationUpload = endUploadTime - startUploadTime;
	console.log('--- Completed Upload file take time:' + durationUpload);
}

function uploadFailed(evt) {
    //alert("There was an error attempting to upload the file.")
}

function uploadCanceled(evt) {
}