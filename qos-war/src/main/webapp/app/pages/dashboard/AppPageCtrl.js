/**
 * Created by NamNH on 6/25/2016.
 */
angular.module('BlurAdmin.pages.dashboard')
  .controller('MapModalCtrl', ['$scope', '$timeout', '$uibModalInstance', 'NgMap' , 'CommonService', MapModalCtrl]);
// https://github.com/sindresorhus/speed-test 
/** @ngInject */
function MapModalCtrl($scope, $timeout, $uibModalInstance, NgMap, CommonService, lat, lng) {
	$scope.lat;
    $scope.lng;
    
    $scope.ok = function () {
      $uibModalInstance.close($scope.link);
    };
    
    (function initController() {
        // init
		//BackendCfg.setupHttp($http);
		CommonService.getLocation(function(result) {
			$scope.geoLocation = result;
			$scope.lat = result.lat;
			$scope.lng = result.lon;
		});
    })();
    
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
//AppPageCtrl.$inject = ['$location', '$scope', '$rootScope', 'AppService', '$timeout', '$interval', '$uibModal'
//                       ,'toastr', 'toastrConfig', 'CommonService', 'sharedService'];
function AppPageCtrl($location, $scope, $rootScope, AppService, $timeout, $interval, $uibModal
						, toastr, toastrConfig, CommonService, sharedService, $sce, $window, BackendCfg) {
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
    var STATUS_BEGIN = -1;
    var STATUS_DOWNLOADING = 0;
    var STATUS_UPLOADING = 1;
    var STATUS_END = 2;
    $scope.statusMeasure;
    
    
    
    var isBusy = false;
    
    $scope.startTimeTest = 0;
    $scope.endTimeTest = 0;
    
    // Config speed and chart
    $scope.speedConfig = {};
    // Config setup chart
    $scope.graphConfig = {};
    // Config setup chart latency
    $scope.latencyGraphConfig = {};
    $scope.uploadFrame;
    
    // 0: Kbps, 1 : Mbps
    $scope.unitType = 1;
    var isErrorPing = false;
    
    $scope.uploadFrame;
    $scope.indexFrame;
    $scope.serverContextUrl;
    (function initController() {
    	$scope.selectedServer = $rootScope.selectedServer;
    	$scope.uploadFrame = $sce.trustAsResourceUrl($rootScope.selectedServer.url + '/upload.html');
    	$scope.serverContextUrl = $sce.trustAsResourceUrl($rootScope.selectedServer.url);
        // ContextPath
        $scope.indexFrame = $sce.trustAsResourceUrl( BackendCfg.contextPath(location) + 'indexUpload.html');
        
    	$timeout(function () {
        	console.log("init AppPageCtrl");
        	
        	// Config speed and chart
            $scope.speedConfig = speedConfig;
            // Config setup chart
            $scope.graphConfig = graphConfig;
            // Config setup chart latency
            $scope.latencyGraphConfig = latencyGraphConfig;
            
            // Init unit
            if ($rootScope.bitrateType.id == 0) {
            	$scope.unitType = 0;
            	// Kbps
            	$scope.speedConfig.yAxis.title.text = 'Kbps';
            	$scope.speedConfig.yAxis.max = 100 * MBPS_TO_KBPS;
            	$scope.speedConfig.yAxis.plotBands[0].to = 40 * MBPS_TO_KBPS;
            	$scope.speedConfig.yAxis.plotBands[1].from = 40 * MBPS_TO_KBPS;
            	$scope.speedConfig.yAxis.plotBands[1].to = 80 * MBPS_TO_KBPS;
            	$scope.speedConfig.yAxis.plotBands[2].from = 80 * MBPS_TO_KBPS;
            	$scope.speedConfig.yAxis.plotBands[2].to = 100 * MBPS_TO_KBPS;
            	
            	$scope.graphConfig.yAxis.title.text = 'Kbps';
            } else {
            	$scope.unitType = 1;
            	$scope.speedConfig.yAxis.title.text = 'Mbps';
            	$scope.speedConfig.yAxis.max = 100;
            	$scope.speedConfig.yAxis.plotBands[0].to = 40;
            	$scope.speedConfig.yAxis.plotBands[1].from = 40;
            	$scope.speedConfig.yAxis.plotBands[1].to = 80;
            	$scope.speedConfig.yAxis.plotBands[2].from = 80;
            	$scope.speedConfig.yAxis.plotBands[2].to = 100;
            	
            	$scope.graphConfig.yAxis.title.text = 'Mbps';
            }
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
        $scope.statusMeasure = STATUS_BEGIN;
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
        data.unitType = $rootScope.bitrateType.id;
    	
    	CommonService.createHistory(data, function(response) {
        	// Toast inform add success or fail
        	$scope.openToastHistory();
    	});
    }
    
    app.stopMeasure = function () {
    	console.log('stop measure speed');
    	
        // cancel
    	app.cancelPromise();
    	
		$scope.endTimeTest = (new Date()).getTime();
		
		// Broadcast item (status: end)
		var broadcastData = {};
		broadcastData.latency = $scope.latencyGraphConfig.series[0].data;
		broadcastData.upload = $scope.graphConfig.series[1].data;
		
		console.log($scope.graphConfig.series[0]);
		console.log($scope.graphConfig.series[1]);
		
		sharedService.prepForBroadcast(2 , 'Speed Test done', broadcastData);

		$scope.showMsg();
		
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
    
    var promiseDownloadData;

    app.retrieveDownloadData = function() {
    	// Get status and data
        var data = $('#DownloadData').val();
        var time = $('#DownloadTime').val();
        // Push into array
        if (data != "") {
        	var number = parseFloat(data);
        	var time = parseFloat(time);
        	var point = [time, number];
        	$scope.graphConfig.series[0].data.push(point);
    		$scope.speedConfig.series[0].data = [number];
        }
    };
    
    app.initChart = function() {
    	// reset data
        $scope.graphConfig.series[0].data = [];
        $scope.graphConfig.series[1].data = [];
        $scope.latencyGraphConfig.series[0].data = [];
        $scope.speedConfig.series[0].data = [0];
        
        // Download data
        $('#DownloadData').val("");
        $('#DownloadTime').val("");
    };
    
    app.startMeasure = function () {
    	console.log('start measure speed');
    	
        // Start time
        $scope.startTimeTest = (new Date()).getTime();
        
        // reset data
        app.initChart()
        
    	$timeout(function () {
    		// stops any running interval to avoid two intervals running at the same time
            app.cancelPromise(); 
            
			$scope.measuring = true;
            $scope.measuringDownload = true;
            $scope.measuringUpload = false;
            
            // First ping
            var serverUrl = $rootScope.selectedServer.url;
        	CommonService.ping(serverUrl + '/api/server/ping', function(responseTime) {
        		if (responseTime == -1) {
        			$scope.openToastErrorPing();
        			app.cancelPromise();
        		} else {
                    isErrorPing = false;
                    
        			// Broadcast item (status: end)
            		sharedService.prepForBroadcast(0 , 'Downloading ... ', {});

            		// store the interval promise
                    promise = $interval(app.measureDownload, 300);
                    
                    promiseDownloadData = $interval(app.retrieveDownloadData, 200);
                    
          		  	// Check upload time to terminate
          		  	$timeout(function () {
          		  		console.log('$timeout after download: ' + MAX_DURATION_TEST/2);
          		  		//Cancel the Timer.
          		        if (angular.isDefined(promise)) {
          		        	$interval.cancel(promise);
          		        }
          		        
          		        if (angular.isDefined(promiseDownloadData)) {
        		        	$interval.cancel(promiseDownloadData);
        		        }
          		      
	          		  	if (!$scope.isAlreadyNotifyUploading) {
	                    	sharedService.prepForBroadcast(1, 'Uploading ...', $scope.graphConfig.series[0].data);
	                    	$scope.isAlreadyNotifyUploading = true;
	                    }
	            			
	            		// Test upload
	            		$scope.measuringDownload = false;
	            		$scope.measuringUpload = true;
	            		
	            		$scope.statusMeasure = STATUS_UPLOADING;
          		  	}, MAX_DURATION_TEST/2);
          		  	
            		promiseLatency = $interval(app.measureLatency, 500);
                    
                    // Update color for chart
                    $scope.speedConfig.yAxis.plotBands[0].color = '#55BF3B';
                    $scope.speedConfig.yAxis.plotBands[1].color = '#DDDF0D';
                    $scope.speedConfig.yAxis.plotBands[2].color = '#DF5353';
                    $scope.speedConfig.series[0].dial.backgroundColor = 'black';
                    $scope.speedConfig.series[0].pivot.backgroundColor = 'black';
        		}
        	});
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
    	
    	var serverUrl = $rootScope.selectedServer.url;
    	CommonService.ping(serverUrl + '/api/server/ping', function(responseTime) {
    		if (responseTime == -1) {
    			isErrorPing = true;
    		} else {
    			$scope.latencyGraphConfig.series[0].data.push(responseTime);
    		}
    	});
    }
    
    app.measureDownload = function () {
    	// Download
//    	var currentTime = (new Date()).getTime();
//    	var cDurration = currentTime - $scope.startTimeTest;
//    	if (cDurration < MAX_DURATION_TEST/2) {
			// Test download
			$scope.measuringDownload = true;
			$scope.measuringUpload = false;
			
			// Download
			$scope.downloadFile(DOWNLOAD_SIZE_SAMPLE);
//    	} else {
//    		if (!$scope.isAlreadyNotifyUploading) {
//            	sharedService.prepForBroadcast(1, 'Uploading ...', $scope.graphConfig.series[0].data);
//            	$scope.isAlreadyNotifyUploading = true;
//            }
//    			
//    		// Test upload
//    		$scope.measuringDownload = false;
//    		$scope.measuringUpload = true;
//    		
//    		$scope.statusMeasure = STATUS_UPLOADING;
    		// Upload
//    		$scope.uploadFile(1024 * 1);
//    		app.uploadCORS();
//    	}
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
    
    // Toast
    var openedToasts = [];
    $scope.options = toastConfig;

    $scope.clearToasts = function () {
      toastr.clear();
    };
    
    $scope.quotesApp = toastQuotesApp;

    $scope.openToastErrorPing = function () {
	      angular.extend(toastrConfig, $scope.options);
    	  var toastType = 'error';
	      var toastQuote = $scope.quotesApp[3];
	      openedToasts.push(toastr[toastType](toastQuote.message, toastQuote.title, toastQuote.options));
	      $scope.optionsStr = "toastr." + toastType + "(\'" + toastQuote.message + "\', \'" + toastQuote.title + "', " + JSON.stringify(toastQuote.options || {}, null, 2) + ")";
    };
  
    $scope.openToast = function () {
      angular.extend(toastrConfig, $scope.options);
      // Info
      var toastType = 'info';
      var toastQuote = $scope.quotesApp[0];
      openedToasts.push(toastr[toastType](toastQuote.message, toastQuote.title, toastQuote.options));
      $scope.optionsStr = "toastr." + toastType + "(\'" + toastQuote.message + "\', \'" + toastQuote.title + "', " + JSON.stringify(toastQuote.options || {}, null, 2) + ")";
      if (!$rootScope.globals.currentUser) {
          // warning
          var toastType = 'warning';
          var toastQuote = $scope.quotesApp[1];
          openedToasts.push(toastr[toastType](toastQuote.message, toastQuote.title, toastQuote.options));
          $scope.optionsStr = "toastr." + toastType + "(\'" + toastQuote.message + "\', \'" + toastQuote.title + "', " + JSON.stringify(toastQuote.options || {}, null, 2) + ")";
      }
    };
    
    $scope.openToastHistory = function () {
        angular.extend(toastrConfig, $scope.options);
        // Info
        var toastType = 'info';
        var toastQuote = $scope.quotesApp[2];
        openedToasts.push(toastr[toastType](toastQuote.message, toastQuote.title, toastQuote.options));
        $scope.optionsStr = "toastr." + toastType + "(\'" + toastQuote.message + "\', \'" + toastQuote.title + "', " + JSON.stringify(toastQuote.options || {}, null, 2) + ")";
      };

    $scope.showMsg = function () {
    	$scope.clearToasts();
    	// Open toast completed
    	$scope.openToast();
    };
    
    $scope.$watch("statusMeasure", function() {
        console.log("statusMeasure: " + $scope.statusMeasure);
        
//        var STATUS_BEGIN = -1;
//        var STATUS_DOWNLOADING = 0;
//        var STATUS_UPLOADING = 1;
//        var STATUS_END = 2;
        if ($scope.statusMeasure == STATUS_UPLOADING) {
        	app.uploadCORS();
        }
    });
    
  //---------------------- Download ---------------------
    $scope.downloadingSpeed;
//    $scope.downloadProgressPercent;
    $scope.downloadEvtLoaded;
    $scope.downloadEvtTotal;
    $scope.durationDownload;
    var startDownloadTime;
    var progressDownloadTime;
    var endDownloadTime;
//    $scope.$watch("downloadingSpeed", function() {
//        console.log("downloadingSpeed: " + $scope.downloadingSpeed);
//        
//        // Calculate and push into chart
////        var downValue = $scope.downloadProgressPercent;
////        var downLoaded = $scope.downloadEvtLoaded;
////        var downTotal = $scope.downloadEvtTotal;
////        var duration = $scope.durationDownload;
////        if (downValue != undefined && downValue > 0) {
////        	var cal = $scope.calculateBandwidth(downLoaded, duration);
//        if ($scope.downloadingSpeed != undefined && $scope.downloadingSpeed > 0) {
//        	$scope.graphConfig.series[0].data.push($scope.downloadingSpeed);
//        	$scope.speedConfig.series[0].data = [$scope.downloadingSpeed];
//        }
////        }
//    });
    
    $scope.downloadFile = function(n) {
    	// Check busy
    	if (isBusy) {
    		return;
    	}
        var oReq = new XMLHttpRequest();
    	var serverUrl = $rootScope.selectedServer.url;

    	oReq.addEventListener("loadstart", transferStart, false);
        oReq.addEventListener("progress", transferProgress);
        oReq.addEventListener("load", transferComplete);
        oReq.addEventListener("error", transferFailed);
//        oReq.addEventListener("abort", transferCanceled);
        
        oReq.open("GET", serverUrl + "/api/file/download?n=" + n + "&rnd=" + new Date().getTime())
        // Start time
        isBusy = true;
        oReq.send();
    }
    
    // progress on transfers from the server to the client (downloads)
    function transferProgress (oEvent) {
	      $scope.$apply(function(){
	            if (oEvent.lengthComputable) {
	                $scope.downloadProgressPercent = Math.round(oEvent.loaded * 100 / oEvent.total);
	                
	                progressDownloadTime = (new Date()).getTime();
	                var downLoaded = oEvent.loaded;
	                var durationDownload = (progressDownloadTime - startDownloadTime);
	                
	                // Calculate
	                var number = calculateBandwidth(downLoaded, durationDownload, $scope.unitType);
//	                var point = [progressDownloadTime, number];
//	                $scope.downloadingSpeed = point;
	                
//	                console.log('---------- transferProgress ------------');
//	                console.log('downLoaded:' + downLoaded);
//	                console.log('durationDownload:' + durationDownload);
//	                console.log('time:' + progressDownloadTime);
//	                console.log('$scope.downloadingSpeed:' + $scope.downloadingSpeed);
	                
	                $('#DownloadData').val(number);
	                $('#DownloadTime').val(progressDownloadTime);
	            } else {
	                $scope.downloadProgressPercent = 'unable to compute';
	                console.log('unable to compute');
	            }
	        })
    }
    
    function transferStart(evt) {
    	startDownloadTime = (new Date()).getTime();
  		console.log('--- Start Download file ----');
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
    app.uploadCORS = function() {
    	// init/reset
    	console.log('trigger click uploadCORS');
    	//var receiverIframe = document.getElementById('UploadIframe').contentWindow;
    	//var receiverIframe = $("#UploadIframe")[0].contentWindow;
    	//var receiverIframe = $(window.UploadIframe.document);
//    	var receiverIframe = document.getElementById("UploadIframe");
    	//var receiverIframe = (x.contentWindow || x.contentDocument);
//    	var receiverIframe = $("#UploadIframe").contents();
//    	var iframe= $('#UploadIframe')[0];
//    	var receiverIframe= iframe.contentWindow? iframe.contentWindow : iframe.contentDocument.defaultView;
//    	console.log(receiverIframe);
    	
//    	var frames = window.frames || window.document.frames;
//    	var aa = frames["UploadIframe"];
//    	var iframeBody = $(frames["UploadIframe"].window.document).contents();
    	
//    	var receiverIframe = $('#UploadIframe')[0];
//    	targetFrame.postMessage('the message', '*');
    	
    	// trigger upload
//    	var postData = {'task': 'upload', 'target_origin' : $rootScope.selectedServer.url};
//    	windowProxy.post(postData);
//    	receiverIframe.postMessage(postData, $rootScope.selectedServer.url);
    	
    	$('#IndexIframe').contents().find('#uploadCORSBtn').trigger( "click" );
    };
    
//    var windowProxy;
//    $window.addEventListener('load', function () {
//    	console.log('$window onload');
//    	// Create a proxy window to send to and receive message from the guest iframe
//    	windowProxy = new Porthole.WindowProxy($rootScope.selectedServer.url + '/proxy.html', 'UploadIframe');
//    	windowProxy.addEventListener(onMessage);
//    });
    
    $window.addEventListener('resize', function () {
    	console.log('resize');
    	$timeout(function () {
    		$('#speedChart').highcharts().reflow();
    		$('#graphChart').highcharts().reflow();
    		$('#latencyGraphConfig').highcharts().reflow();
    		$scope.$apply();
    	}, 200);
	});
    
    //---------------------- Upload ---------------------
    $window.addEventListener('message', function(event){
        if (typeof(event.data) !== 'undefined'){
        	console.log('==== event.data=====');
        	console.log(event.data);
        	var task = event.data.task;
        	var result = event.data.data;
        	var time = event.data.time;
        	
        	switch ( task ) { // postMessage tasks
		        // update chart received in postMessage
		        case 'uploading' :
		            // Push into array
	            	$scope.$apply(function(){
	            		var number = parseFloat(result);
	            		
	            		// Check first time
	            		if ($scope.graphConfig.series[1].data.length == 0 && $scope.graphConfig.series[0].data.length != 0) {
	            			// Get time latest of Download chart
	            			var latestDownloadPoint = $scope.graphConfig.series[0].data[$scope.graphConfig.series[0].data.length - 1];
	            			time = latestDownloadPoint[0];
	            		}
	            		
	            		var point = [time, number];
	            		$scope.graphConfig.series[1].data.push(point);
		        		$scope.speedConfig.series[0].data = [number];
	            	});
		            break;
		
		        case 'completed' :
		        	$scope.$apply(function(){
	            		var number = parseFloat(result);
	            		var point = [time, number];
	            		$scope.graphConfig.series[1].data.push(point);
		        		$scope.speedConfig.series[0].data = [number];
	            	});
	        		
		            // Stop
		        	app.stopMeasure();
		            break;
		        
		        //default:
		    }
           // handle message
        }
    });
    
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