/**
 * Created by NamNH on 6/25/2016.
 */
angular.module('BlurAdmin.pages.dashboard')
  .controller('MapModalCtrl', ['$scope', '$timeout', '$uibModalInstance', 'NgMap', MapModalCtrl]);
// https://github.com/sindresorhus/speed-test 
/** @ngInject */
function MapModalCtrl($scope, $timeout, $uibModalInstance, NgMap, lat, lng) {
    $scope.lat = 21.0333;
    $scope.lng = 105.85;
    $scope.ok = function () {
      $uibModalInstance.close($scope.link);
    };
    
    $scope.$on('mapInitialized', function(event, map) {
    	window.setTimeout(function () 
		{
			var center = new google.maps.LatLng($scope.lat, $scope.lng);
			window.google.maps.event.trigger(map,
					'resize');
			map.setCenter(center);
		}, 100);
    }); 
};
  
angular.module('BlurAdmin.pages.dashboard')
	.controller('GeoLocationPageCtrl', GeoLocationPageCtrl);
function GeoLocationPageCtrl($scope, $rootScope, $http, BackendCfg, CommonService) {
	var geo = this;
	$scope.geoLocation = {};
	
	(function initController() {
        // init
		//BackendCfg.setupHttp($http);
		CommonService.getLocation(function(result) {
			$scope.geoLocation = result;
			$rootScope.geoLocation = result;
		});
    })();
};

angular.module('BlurAdmin.pages.dashboard')
	.controller('AppPageCtrl', AppPageCtrl);
AppPageCtrl.$inject = ['$location', '$scope', '$rootScope', 'AppService', '$timeout', '$interval', '$uibModal'
                       ,'toastr', 'toastrConfig', 'CommonService', 'sharedService'];
function AppPageCtrl($location, $scope, $rootScope, AppService, $timeout, $interval, $uibModal
						, toastr, toastrConfig, CommonService, sharedService) {
	console.log("app controller");
	var app = this;
	var defaultConfig = angular.copy(toastrConfig);
	$scope.selectedServer = {};
    $scope.timeInMs = 0;
    $scope.measuring = false;
    $scope.measuringDownload = false;
    $scope.measuringUpload = false;
    $scope.isAlreadyNotifyUploading = false;
    // store the interval promise in this variable
    var promise;
    var promiseLatency;
    // For share broadcast
    $scope.message = '';
    // Broadcast status (-1: Begin , 0: Downloading, 1: Uploading, 2: End)
    
    var isBusy = false;
    
    var MAX_DURATION_TEST = 10 * 1000; // ms
    $scope.startTimeTest = 0;
    $scope.endTimeTest = 0;
    
    // Config speed and chart
    $scope.speedConfig = {};
    // Config setup chart
    $scope.graphConfig = {};
    // Config setup chart latency
    $scope.latencyGraphConfig = {};
    
    (function initController() {
    	$scope.selectedServer = $rootScope.selectedServer;
    	$timeout(function () {
        	console.log("init AppPageCtrl");
        	// Config speed and chart
            $scope.speedConfig = speedConfig;
            // Config setup chart
            $scope.graphConfig = graphConfig;
            // Config setup chart latency
            $scope.latencyGraphConfig = latencyGraphConfig;
        }, 500);
        // init
    })();
    
    app.cancelPromise = function () {
    	//Cancel the Timer.
        if (angular.isDefined(promise)) {
        	$interval.cancel(promise);
        }
        
        if (angular.isDefined(promiseLatency)) {
        	$interval.cancel(promiseLatency);
        }
        
        // Reset
    	$scope.measuring = false;
    	$scope.measuringDownload = false;
        $scope.measuringUpload = false;
        $scope.isAlreadyNotifyUploading = false;
    }
    
    app.addHistory = function () {
    	// Check user login
    	if (!$rootScope.globals.currentUser) {
    		return;
    	}
    	var data = {};
    	data.download = $scope.graphConfig.series[0].data;
        data.upload = $scope.graphConfig.series[1].data;
        data.latency = $scope.latencyGraphConfig.series[0].data;
        data.startTime = $scope.startTimeTest;
        data.endTime = $scope.endTimeTest;
    	
    	CommonService.createHistory(data, function(response) {
        	// Toast inform add success or fail
        	$scope.openToastHistory();
    	});
    }
    
    app.stopMeasure = function () {
    	console.log('stop measure speed');
    	
        // cancel
    	app.cancelPromise();
    	
		$scope.showMsg();
		
		$scope.endTimeTest = (new Date()).getTime();
		
		// Broadcast item (status: end)
		var broadcastData = {};
		broadcastData.latency = $scope.latencyGraphConfig.series[0].data;
		broadcastData.upload = $scope.graphConfig.series[1].data;
		
		sharedService.prepForBroadcast(2 , 'Speed Test done', broadcastData);
		
		// Add to history
		app.addHistory();
		
		// Update color for chart
        $scope.speedConfig.yAxis.plotBands[0].color = '#c4c4c4';
        $scope.speedConfig.yAxis.plotBands[1].color = '#c4c4c4';
        $scope.speedConfig.yAxis.plotBands[2].color = '#c4c4c4';
        $scope.speedConfig.series[0].dial.backgroundColor = '#c4c4c4';
        $scope.speedConfig.series[0].pivot.backgroundColor = '#c4c4c4';
        $scope.speedConfig.series[0].data = [0];
    }
    
    app.startMeasure = function () {
    	console.log('start measure speed');

        // Start time
        $scope.startTimeTest = (new Date()).getTime();
        
        // reset data
        $scope.graphConfig.series[0].data = [];
        $scope.graphConfig.series[1].data = [];
        $scope.latencyGraphConfig.series[0].data = [];
        $scope.speedConfig.series[0].data = [0];
        
    	$timeout(function () {
    		// stops any running interval to avoid two intervals running at the same time
            app.cancelPromise(); 

        	$scope.measuring = true;
            $scope.measuringDownload = true;
            $scope.measuringUpload = false;
            
    		// Broadcast item (status: end)
    		sharedService.prepForBroadcast(0 , 'Downloading ... ', {});
    		
            // store the interval promise
            promise = $interval(app.measureSpeed, 500);
    		promiseLatency = $interval(app.measureLatency, 500);
    		//app.measureSpeed();
            
            // Update color for chart
            $scope.speedConfig.yAxis.plotBands[0].color = '#55BF3B';
            $scope.speedConfig.yAxis.plotBands[1].color = '#DDDF0D';
            $scope.speedConfig.yAxis.plotBands[2].color = '#DF5353';
            $scope.speedConfig.series[0].dial.backgroundColor = 'black';
            $scope.speedConfig.series[0].pivot.backgroundColor = 'black';
    	}, 300);
    }
    
    app.measureLatency = function () {
    	// Breake
    	var currentTime = (new Date()).getTime();
    	var duration = currentTime - $scope.startTimeTest;
    	// Completed test
    	if (duration > MAX_DURATION_TEST) {
    		app.stopMeasure();
    		return;
    	}
    	
    	CommonService.ping(SERVER_URL + '/api/server/ping', function(responseTime) {
    		$scope.latencyGraphConfig.series[0].data.push(responseTime);
    	});
    }
    
    app.measureSpeed = function () {
    	// Download
    	var currentTime = (new Date()).getTime();
    	var cDurration = currentTime - $scope.startTimeTest;
    	if (cDurration < MAX_DURATION_TEST/2) {
    		// Test download
    		$scope.measuringDownload = true;
    		$scope.measuringUpload = false;
    		
    		// Download
    		$scope.downloadFile(1024);
    	} else {
    		if (!$scope.isAlreadyNotifyUploading) {
            	sharedService.prepForBroadcast(1, 'Uploading ...', $scope.graphConfig.series[0].data);
            	$scope.isAlreadyNotifyUploading = true;
            }
    			
    		// Test upload
    		$scope.measuringDownload = false;
    		$scope.measuringUpload = true;
    		
    		// Upload
    		$scope.uploadFile(1024 * 4);
    	}
    }
    
    // Setting
    $scope.openModal = function (size) {
	      var uibModalInstance = $uibModal.open({
	        animation: true,
	        templateUrl: 'app/pages/dashboard/mapModal.html',
	        controller: 'MapModalCtrl',
	        size: size,
	        resolve: {
	        	lat : $rootScope.geoLocation.lat,
	      		lng : $rootScope.geoLocation.lon
	        }
	      });
	
	      uibModalInstance.result.then(function (selectedItem) {
	    	  $scope.selected = selectedItem;
	      }, function () {
	    	  console.log('Modal dismissed at: ' + new Date());
	      });
    };
    
    $scope.toggleAnimation = function () {
    	$scope.animationsEnabled = !$scope.animationsEnabled;
    };

    var openedToasts = [];
    $scope.options = {
      autoDismiss: false,
      positionClass: 'toast-bottom-center',
      type: 'info',
      timeOut: '3000',
      extendedTimeOut: '1000',
      allowHtml: false,
      closeButton: false,
      tapToDismiss: true,
      progressBar: true,
      newestOnTop: true,
      maxOpened: 0,
      preventDuplicates: false,
      preventOpenDuplicates: false,
      escapeHtml : true,
      title: "Well done!",
      msg: "You just run speed test completed."
    };

    $scope.clearToasts = function () {
      toastr.clear();
    };
    
    $scope.quotes = [
         {
           title: 'Well done!',
           message: 'You just run speed test completed.',
           options: {
             allowHtml: true
           }
         },
         {
           title: 'No <em>login</em> yet!',
           message: 'Please <strong><a href="#/login">login</a></strong> to show your history',
           options: {
             allowHtml: true
           }
         },
         {
             title: 'Good job!',
             message: 'A history record added into system successfull.',
             options: {
               allowHtml: true
             }
           }
       ];

    $scope.openToast = function () {
      angular.extend(toastrConfig, $scope.options);
      // Info
      var toastType = 'info';
      var toastQuote = $scope.quotes[0];
      openedToasts.push(toastr[toastType](toastQuote.message, toastQuote.title, toastQuote.options));
      $scope.optionsStr = "toastr." + toastType + "(\'" + toastQuote.message + "\', \'" + toastQuote.title + "', " + JSON.stringify(toastQuote.options || {}, null, 2) + ")";
      if (!$rootScope.globals.currentUser) {
          // warning
          var toastType = 'warning';
          var toastQuote = $scope.quotes[1];
          openedToasts.push(toastr[toastType](toastQuote.message, toastQuote.title, toastQuote.options));
          $scope.optionsStr = "toastr." + toastType + "(\'" + toastQuote.message + "\', \'" + toastQuote.title + "', " + JSON.stringify(toastQuote.options || {}, null, 2) + ")";
      }
    };
    
    $scope.openToastHistory = function () {
        angular.extend(toastrConfig, $scope.options);
        // Info
        var toastType = 'info';
        var toastQuote = $scope.quotes[2];
        openedToasts.push(toastr[toastType](toastQuote.message, toastQuote.title, toastQuote.options));
        $scope.optionsStr = "toastr." + toastType + "(\'" + toastQuote.message + "\', \'" + toastQuote.title + "', " + JSON.stringify(toastQuote.options || {}, null, 2) + ")";
      };

    $scope.showMsg = function () {
    	$scope.clearToasts();
    	// Open toast completed
    	$scope.openToast();
    };
    
  //---------------------- Download ---------------------
    $scope.downloadProgressPercent;
    $scope.downloadEvtLoaded;
    $scope.downloadEvtTotal;
    $scope.durationDownload;
    var startDownloadTime;
    var progressDownloadTime;
    var endDownloadTime;
    $scope.$watch("downloadProgressPercent", function() {
        console.log("downloadProgressPercent: " + $scope.downloadProgressPercent);
        
        // Calculate and push into chart
        var downValue = $scope.downloadProgressPercent;
        var downLoaded = $scope.downloadEvtLoaded;
        var downTotal = $scope.downloadEvtTotal;
        var duration = $scope.durationDownload;
        if (downValue != undefined && downValue > 0) {
        	var cal = $scope.calculateBandwidth(downLoaded, duration);
        	$scope.graphConfig.series[0].data.push(cal);
        	$scope.speedConfig.series[0].data = [cal];
        }
    });
    
    $scope.downloadFile = function(n) {
    	// Check busy
    	if (isBusy) {
    		return;
    	}
        var oReq = new XMLHttpRequest();

        oReq.addEventListener("progress", updateProgress);
        oReq.addEventListener("load", transferComplete);
        oReq.addEventListener("error", transferFailed);
        oReq.addEventListener("abort", transferCanceled);

        oReq.open("GET", SERVER_URL + "/api/file/download?n=" + n)
        // Start time
        isBusy = true;
        startDownloadTime = (new Date()).getTime();
        oReq.send();
    }
    
    // progress on transfers from the server to the client (downloads)
    function updateProgress (oEvent) {
//	      if (oEvent.lengthComputable) {
//	        var percentComplete = oEvent.loaded / oEvent.total;
//	        // ...
//	      } else {
//	        // Unable to compute progress information since the total size is unknown
//	      }
	      $scope.$apply(function(){
	            if (oEvent.lengthComputable) {
	                $scope.downloadProgressPercent = Math.round(oEvent.loaded * 100 / oEvent.total)
	                progressDownloadTime = (new Date()).getTime();
	                $scope.downloadEvtLoaded = oEvent.loaded;
	                $scope.downloadEvtTotal = oEvent.total;
	                
	                $scope.durationDownload = (progressDownloadTime - startDownloadTime);
	            } else {
	                $scope.downloadProgressPercent = 'unable to compute';
	                console.log('unable to compute');
	            }
	        })
    }

    function transferComplete(evt) {
    	isBusy = false;
    	endDownloadTime = (new Date()).getTime();
  		$scope.durationDownload = endDownloadTime - startDownloadTime;
  		console.log('--- Completed Download file take time:' + $scope.durationDownload);
    }

    function transferFailed(evt) {
      console.log("An error occurred while transferring the file.");
    }

    function transferCanceled(evt) {
      console.log("The transfer has been canceled by the user.");
    }
    // --------------------------------------------------
    
    //---------------------- Upload ---------------------
    $scope.uploadProgressPercent;
    $scope.uploadEvtLoaded;
    $scope.uploadEvtTotal;
    $scope.durationUpload;
    var startUploadTime;
    var progressUploadTime;
    var endUploadTime;
    $scope.$watch("uploadProgressPercent", function() {
        console.log("uploadProgressPercent: " + $scope.uploadProgressPercent);
        
        // Calculate and push into chart
        var upValue = $scope.uploadProgressPercent;
        var upLoaded = $scope.uploadEvtLoaded;
        var upTotal = $scope.uploadEvtTotal;
        var duration = $scope.durationUpload;
        if (upValue != undefined && upValue > 0) {
        	var cal = $scope.calculateBandwidth(upLoaded, duration);
        	$scope.graphConfig.series[1].data.push(cal);
        	$scope.speedConfig.series[0].data = [cal];
        }
    });
    
    $scope.calculateBandwidth = function (loadedSize, duration) {
    	return (loadedSize / (1024 * 1024))/ duration * 1000;
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
    
    $scope.uploadFile = function(size) {
    	// Check busy
    	if (isBusy) {
    		return;
    	}
        var fd = new FormData();
        
        // 1024k
        var uploadData = $scope.randomString(1024 * size);
		var filename = "upload" + (new Date).getTime() + ".bin";
        var file = new File([uploadData], filename);
        fd.append("file", file)
        
        var xhr = new XMLHttpRequest();
        xhr.upload.addEventListener("progress", uploadProgress, false)
        xhr.addEventListener("load", uploadComplete, false)
        xhr.addEventListener("error", uploadFailed, false)
        xhr.addEventListener("abort", uploadCanceled, false)
        xhr.open("POST", SERVER_URL + "/api/file/upload")
        $scope.uploadProgressPercentVisible = true

        // Start time
        isBusy = true;
        startUploadTime = (new Date()).getTime();
        xhr.send(fd)
    }
    
    function uploadProgress(evt) {
        $scope.$apply(function(){
            if (evt.lengthComputable) {
                $scope.uploadProgressPercent = Math.round(evt.loaded * 100 / evt.total)
                progressUploadTime = (new Date()).getTime();
                $scope.uploadEvtLoaded = evt.loaded;
                $scope.uploadEvtTotal = evt.total;
                
                $scope.durationUpload = (progressUploadTime - startUploadTime);
            } else {
                $scope.uploadProgressPercent = 'unable to compute';
                console.log('unable to compute');
            }
        })
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
    
    // --------------------------------------------------
    
    $scope.$on('$destroy', function() {
        // Make sure that the interval is destroyed too
    	app.cancelPromise();
    	
        // reset data
        $scope.measuring = false;
        $scope.graphConfig.series[0].data = [];
        $scope.graphConfig.series[1].data = [];
        $scope.latencyGraphConfig.series[0].data = [];
        $scope.speedConfig.series[0].data = [0];
    });
    
    $scope.$on('handleBroadcast', function() {
        $scope.message = sharedService.message;
    });
};