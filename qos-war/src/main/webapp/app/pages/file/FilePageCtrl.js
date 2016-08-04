/**
 * Created by NamNH on 6/25/2016.
 */

angular.module('BlurAdmin.pages.file')
	.controller('FilePageCtrl', FilePageCtrl);

function FilePageCtrl($location, $scope, $rootScope, AppService, $timeout, $interval, $uibModal
						, toastr, toastrConfig, CommonService, sharedService, BackendCfg) {
	console.log("file controller");
	var file = this;
	//---------------------- Upload ---------------------
    $scope.uploadingSpeed;
    $scope.uploadProgressPercent;
    $scope.uploadEvtLoaded;
    $scope.uploadEvtTotal;
    $scope.durationUpload;
    var startUploadTime;
    var progressUploadTime;
    var endUploadTime;
    
    var isBusy = false;
    $scope.$watch("uploadingSpeed", function() {
        console.log("---- uploadingSpeed: " + $scope.uploadingSpeed);
        
        // Calculate and push into chart
//        var upValue = $scope.uploadProgressPercent;
//        var upLoaded = $scope.uploadEvtLoaded;
//        var upTotal = $scope.uploadEvtTotal;
//        var duration = $scope.durationUpload;
//        if (upValue != undefined && upValue > 0) {
//        	var cal = $scope.calculateBandwidth(upLoaded, duration);
//        	$scope.graphConfig.series[1].data.push(cal);
//        	$scope.speedConfig.series[0].data = [cal];
//        }
        if ($scope.uploadingSpeed != undefined && $scope.uploadingSpeed > 0) {
        	//$scope.graphConfig.series[1].data.push($scope.uploadingSpeed);
    		//$scope.speedConfig.series[0].data = [$scope.uploadingSpeed];
        }
    });
    
    $scope.calculateBandwidth = function (loadedSize, duration) {
    	// Mbps
    	if ($scope.unitType == 1) {
    		return (loadedSize / (1024 * 1000))/ (duration / 1000);
    	} else {
    		return (loadedSize / 1024)/ (duration / 1000);
    	}
    };
    
    $scope.randomString = function (len) {
        var charSet = 'ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789';
        var randomString = '';
        for (var i = 0; i < len; i++) {
        	var randomPoz = Math.floor(Math.random() * charSet.length);
        	randomString += charSet.substring(randomPoz,randomPoz+1);
        }
        return randomString;
    }
    
    $scope.startUpload = function() {
    	// Check busy
    	if (isBusy) {
    		return;
    	}
        var fd = new FormData();
        
        // 1024k
        var size = 1024 * 4;
        var uploadData = $scope.randomString(1024 * size);
		var filename = "upload" + (new Date).getTime() + ".bin";
        var file = new File([uploadData], filename);
        fd.append("file", file)
        
        var serverUrl = $rootScope.selectedServer.url;
        $.ajax({
		    xhr: function() {
		        var xhr = new window.XMLHttpRequest();
//		        xhr.upload.addEventListener("loadstart", uploadStart, false);
		        xhr.upload.addEventListener("progress", uploadProgress, false);
//		        xhr.upload.addEventListener("load", uploadComplete, false);
//		        xhr.upload.addEventListener("error", uploadFailed, false);
//		        xhr.upload.addEventListener("abort", uploadCanceled, false);
		
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
//		    	xmlHttpRequest.withCredentials = true;
                startUploadTime = new Date().getTime();
            }
		});
    }
    
    function uploadProgress(evt) {
        $scope.$apply(function(){
            if (evt.lengthComputable) {
                $scope.uploadProgressPercent = Math.round(evt.loaded * 100 / evt.total)
                progressUploadTime = (new Date()).getTime();
                
                var loaded = evt.loaded;
                var durationUpload = (progressUploadTime - startUploadTime);
                console.log('------------ uploadProgress');
                console.log('------------ loaded:' + loaded);
                console.log('------------ durationUpload:' + durationUpload);
                
                // Calculate
                $scope.uploadingSpeed = $scope.calculateBandwidth(loaded, durationUpload);
            } else {
                $scope.uploadProgressPercent = 'unable to compute';
                console.log('unable to compute');
            }
        })
    }
    
    function uploadStart(evt) {
        startUploadTime = (new Date()).getTime();
    	console.log('--- Start Upload file take time --');
    }

    function uploadComplete(evt) {
    	isBusy = false;
        /* This event is raised when the server send back a response */
        //alert(evt.target.responseText)
    	endUploadTime = (new Date()).getTime();
    	$scope.durationUpload = endUploadTime - startUploadTime;
    	console.log('--- Completed Upload file take time:' + $scope.durationUpload);
    }

    function uploadFailed(evt) {
        //alert("There was an error attempting to upload the file.")
    }

    function uploadCanceled(evt) {
        $scope.$apply(function(){
            $scope.uploadProgressPercentVisible = false
        })
        //alert("The upload has been canceled by the user or the browser dropped the connection.")
    }
};