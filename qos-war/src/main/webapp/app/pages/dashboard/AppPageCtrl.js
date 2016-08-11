/**
 * Created by NamNH on 6/25/2016.
 */
angular.module('BlurAdmin.pages.dashboard')
	.controller('AppPageCtrl', AppPageCtrl);
function AppPageCtrl($location, $scope, $rootScope, AppService, $timeout, $interval, $uibModal
						, toastr, toastrConfig, CommonService, sharedService, $sce, $window, BackendCfg, Upload) {
	console.log("app controller");
	var app = this;
	var defaultConfig = angular.copy(toastrConfig);
	$scope.selectedServer = {};
    $scope.timeInMs = 0;
    $scope.measuring = false;
    $scope.measuringDownload = false;
    $scope.measuringUpload = false;
    
    
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
        
        if (angular.isDefined(promiseUpload)) {
        	$interval.cancel(promiseUpload);
        }
        
        if (angular.isDefined(promiseUploadData)) {
        	$interval.cancel(promiseUploadData);
        }
        
        // Reset
    	$scope.measuring = false;
    	$scope.measuringDownload = false;
        $scope.measuringUpload = false;
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
    	
        if (!app.validateHistoryData(data)) {
        	$scope.openToastHistory('history-data-invalid');
        	return;
        }
        
    	CommonService.createHistory(data, function(response) {
        	// Toast inform add success or fail
        	$scope.openToastHistory('create-history-success');
    	});
    }
    
    app.validateHistoryData = function (data) {
    	return true;
    };
    
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
		
		// Stop uploading if any
		if (file) {
			console.log('--- abort upload -----');
			file.upload.abort();
		}

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
        if (data != "" && parseFloat(data) > 0) {
        	var number = parseFloat(data);
        	var time = parseFloat(time);
        	var point = [time, number];
        	$scope.graphConfig.series[0].data.push(point);
        	
        	var speedNumber = number;
        	if ($scope.unitType == 1 && speedNumber > MAX_BANDWIDTH_MBPS) {
        		speedNumber = MAX_BANDWIDTH_MBPS;
        	} else if ($scope.unitType == 0 && speedNumber > MAX_BANDWIDTH_MBPS * MBPS_TO_KBPS) {
        		speedNumber = MAX_BANDWIDTH_MBPS * MBPS_TO_KBPS;
        	}
    		$scope.speedConfig.series[0].data = [speedNumber];
        }
    };
    
    app.retrieveUploadData = function() {
    	// Get status and data
        var data = $('#UploadData').val();
        var time = $('#UploadTime').val();
        // Push into array
        if (data != "" && parseFloat(data)) {
        	var number = parseFloat(data);
        	var time = parseFloat(time);
        	var point = [time, number];
        	
        	if (!skipUploadPoint) {
        		skipUploadPoint = true;
        		return;
        	}
        	
        	// normalizedDataPoint
        	var latPoint;
        	if ($scope.graphConfig.series[1].data.length != 0) {
        		latPoint = $scope.graphConfig.series[1].data[$scope.graphConfig.series[1].data.length - 1];
        		point = normalizedDataPoint(latPoint, point, $scope.unitType);
        	}
        	 
        	$scope.graphConfig.series[1].data.push(point);
        	
        	var speedNumber = number;
        	if ($scope.unitType == 1 && speedNumber > MAX_BANDWIDTH_MBPS) {
        		speedNumber = MAX_BANDWIDTH_MBPS;
        	} else if ($scope.unitType == 0 && speedNumber > MAX_BANDWIDTH_MBPS * MBPS_TO_KBPS) {
        		speedNumber = MAX_BANDWIDTH_MBPS * MBPS_TO_KBPS;
        	}
    		$scope.speedConfig.series[0].data = [speedNumber];
        }
    };
    
    app.resetInitData = function() {
    	// reset data
        $scope.graphConfig.series[0].data = [];
        $scope.graphConfig.series[1].data = [];
        $scope.latencyGraphConfig.series[0].data = [];
        $scope.speedConfig.series[0].data = [0];
        
        // Download data
        $('#DownloadData').val("");
        $('#DownloadTime').val("");
        $('#UploadData').val("");
        $('#UploadTime').val("");
        
        isBusy = false;
    };
    
    app.startMeasure = function () {
    	console.log('start measure speed');
    	
        // Start time
        $scope.startTimeTest = (new Date()).getTime();
        
        // reset data
        app.resetInitData();
        
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
                    skipUploadPoint = false;
                    
                    // Update color for chart
                    $scope.speedConfig.yAxis.plotBands[0].color = '#55BF3B';
                    $scope.speedConfig.yAxis.plotBands[1].color = '#DDDF0D';
                    $scope.speedConfig.yAxis.plotBands[2].color = '#DF5353';
                    $scope.speedConfig.series[0].dial.backgroundColor = 'black';
                    $scope.speedConfig.series[0].pivot.backgroundColor = 'black';
                    
        			// Broadcast item (status: end)
            		sharedService.prepForBroadcast(0 , 'Downloading ... ', {});

            		// store the interval promise
                    promise = $interval(app.measureDownload, 300);
                    
                    promiseDownloadData = $interval(app.retrieveDownloadData, 200);
                    
          		  	// Check upload time to terminate
          		  	$timeout(function () {
          		  		console.log('$timeout after download: ' + MAX_DURATION_TEST/2);
          		  		//Cancel the Timer .
          		        if (angular.isDefined(promise)) {
          		        	$interval.cancel(promise);
          		        }
          		        
          		        if (angular.isDefined(promiseDownloadData)) {
        		        	$interval.cancel(promiseDownloadData);
        		        }
          		        
	          			if (dReq && dReq.readyState != 4) {
	          				console.log('-----abort xhr-----');
	          				dReq.abort();
	          			}
	          			isBusy = false;
	          			
//	          		  	if (!$scope.isAlreadyNotifyUploading) {
          		        // Broad cast to inform end download, start upload
                    	sharedService.prepForBroadcast(1, 'Uploading ...', $scope.graphConfig.series[0].data);
//	                    	$scope.isAlreadyNotifyUploading = true;
//	                    }
	            			
	            		// Test upload
	            		$scope.measuringDownload = false;
	            		$scope.measuringUpload = true;
	            		
//	            		$scope.statusMeasure = STATUS_UPLOADING;
	            		promiseUpload = $interval(app.uploadDataCORS, 300);
	            		promiseUploadData = $interval(app.retrieveUploadData, 200);
          		  	}, MAX_DURATION_TEST / 2);
          		  	
            		promiseLatency = $interval(app.measureLatency, 1000);
            		
            		// STOP stest
            		$timeout(function () {
            			app.stopMeasure();
          		  	}, MAX_DURATION_TEST + 1000);
        		}
        	});
    	}, 300);
    }
    
    app.measureLatency = function () {
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
    
  //---------------------- Download ---------------------
    $scope.downloadingSpeed;
    $scope.downloadEvtLoaded;
    $scope.downloadEvtTotal;
    $scope.durationDownload;
    var startDownloadTime;
    var progressDownloadTime;
    var endDownloadTime;
    
    var dReq;
    $scope.downloadFile = function(n) {
    	// Check busy
    	if (isBusy) {
    		return;
    	}
    	var serverUrl = $rootScope.selectedServer.url;
    	
    	dReq = $.ajax({
    	    xhr: function() {
    	        var xhr = new window.XMLHttpRequest();
    	        xhr.addEventListener("loadstart", transferStart, false);
    	        xhr.addEventListener("progress", transferProgress);
    	        xhr.addEventListener("load", transferComplete);
    	        xhr.addEventListener("error", transferFailed);
    	
    	       return xhr;
    	    },
    	    cache: false,
//            contentType: false,
//            processData: false,
    	    type: 'GET',
    	    url: serverUrl + "/api/file/download?n=" + n + "&rnd=" + new Date().getTime(),
    	    success: function(data){
    	        //Do something on success
    	        console.log('download done');
    	    },
    	    xhrFields: {
    	        //withCredentials: true,
    	    },
    	    beforeSend: function(xmlHttpRequest) {
//    	    	xmlHttpRequest.withCredentials = true;
//                startUploadTime = new Date().getTime();
            }
    	});
    	
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
	                
	                $('#DownloadData').val(number);
	                $('#DownloadTime').val(progressDownloadTime);
	            } else {
	                $scope.downloadProgressPercent = 'unable to compute';
	                console.log('unable to compute');
	            }
	        })
    }
    
    function transferStart(evt) {
        isBusy = true;
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
    var promiseUpload;
    var promiseUploadData;
    var file;
    var skipUploadPoint;
    app.uploadDataCORS = function() {
    	if (isBusy) {
        	return;
        }
    	// init/reset
    	console.log('uploadDataCORS');
    	
        var uploadData = randomString(1024 * UPLOAD_SIZE_SAMPLE);
    	var filename = "upload" + (new Date).getTime() + ".bin";
        //file = new File([uploadData], filename);
		file = new Blob([uploadData], {type: 'application/json'});
    	
        uploadUsing$http(file);
    };
    
    
    //---------------------- Upload ---------------------
    var startUploadTime;
    function uploadUsing$http(file) {
    	var serverUrl = $rootScope.selectedServer.url + '/app/upload';
    	
        file.upload = Upload.http({
          url: serverUrl,
          method: 'POST',
          headers: {
            'Content-Type': file.type
          },
          data: file
        });

        file.upload.then(function (response) {
          file.result = response.data;
          isBusy = false;
          console.log('Upload complete');
        }, function (response) {
          if (response.status > 0)
            $scope.errorMsg = response.status + ': ' + response.data;
        });

        file.upload.xhr(function (xhr) {
        	 xhr.upload.addEventListener('loadstart', function(){
        		 console.log('----------- Start upload');
        		 isBusy = true;
        		 startUploadTime = new Date().getTime();
        	 }, false);
        });
        
        file.upload.progress(function (evt) {
        	if (evt.lengthComputable) {
        		file.progress = Math.min(100, parseInt(100.0 * evt.loaded / evt.total));
                
                var progressUploadTime = (new Date()).getTime();
                var loaded = evt.loaded;
                var durationUpload = (progressUploadTime - startUploadTime);
                
                if (durationUpload == 0 || loaded == 0) {
                	return;
                }
                // Calculate
                uploadingSpeed = calculateBandwidth(loaded, durationUpload, $scope.unitType);
                
                $('#UploadData').val(uploadingSpeed);
                $('#UploadTime').val(progressUploadTime);
            } else {
                console.log('unable to compute upload');
            }
        });
    }
    
    //-------------------- Toast ---------------------------
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
    
    $scope.openToastHistory = function (type) {
        angular.extend(toastrConfig, $scope.options);
        
        if (type == 'create-history-success') {
        		var toastType = 'info';
            	var toastQuote = $scope.quotesApp[2];
	      } else if (type == 'history-data-invalid') {
	    	  var toastType = 'error';
		      var toastQuote = $scope.quotes[4];
	      }
        // Info
        openedToasts.push(toastr[toastType](toastQuote.message, toastQuote.title, toastQuote.options));
        $scope.optionsStr = "toastr." + toastType + "(\'" + toastQuote.message + "\', \'" + toastQuote.title + "', " + JSON.stringify(toastQuote.options || {}, null, 2) + ")";
      };

    $scope.showMsg = function () {
    	$scope.clearToasts();
    	// Open toast completed
    	$scope.openToast();
    };
    
    // ------------------- Window event -------------------------------
    $window.addEventListener('resize', function () {
    	console.log('resize');
    	$timeout(function () {
    		$('#speedChart').highcharts().reflow();
    		$('#graphChart').highcharts().reflow();
    		$('#latencyGraphConfig').highcharts().reflow();
    		$scope.$apply();
    	}, 200);
	});
    
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