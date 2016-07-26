/**
 * @author Nam, Nguyen Hoai
 * created on 16.12.2015
 * http://jsfiddle.net/simpulton/XqDxG/
 */
(function () {
  'use strict';

  angular.module('BlurAdmin.pages.dashboard')
  .controller('InfoModalCtrl', ['$scope', '$timeout', '$uibModalInstance', 'deviceDetector', 'CommonService', InfoModalCtrl]);


  /** @ngInject */
  function InfoModalCtrl($scope, $timeout, $uibModalInstance, deviceDetector, CommonService ) {
	  $scope.data = deviceDetector;
	  $scope.allData = JSON.stringify($scope.data, null, 2);
	  $scope.geoLocation;
	  $scope.link = '';
	  $scope.ok = function () {
		  $uibModalInstance.close($scope.link);
	  };
    
    (function initController() {
    	$timeout(function () {
	    	CommonService.getLocation(function (response) {
	    		$scope.geoLocation = response;
	        });
    	}, 300);
	})();
  }
  
  angular.module('BlurAdmin.pages.dashboard')
  .controller('DashboardPieChartCtrl', ['$scope', '$timeout', 'baConfig', 'baUtil', 'deviceDetector', 'CommonService', '$uibModal', 'sharedService', DashboardPieChartCtrl]);
  
  /** @ngInject */
  function DashboardPieChartCtrl($scope, $timeout, baConfig, baUtil, deviceDetector, CommonService, $uibModal, sharedService) {
    var pieColor = baUtil.hexToRGB(baConfig.colors.defaultText, 0.2);
    $scope.data = deviceDetector;
    $scope.allData = JSON.stringify($scope.data, null, 2);
    $scope.geoLocation = {};
    $scope.message = '';
    $scope.status;
	
    $scope.pieColor = pieColor;
    $scope.charts = [{
      color: pieColor,
      description: 'Download',
      stats: '--',
      average: '--',
      unit: 'Mb/s',
      icon: 'download',
      loading: false
    }, {
      color: pieColor,
      description: 'Upload',
      stats: '--',
      average: '--',
      unit: 'Mb/s',
      icon: 'upload',
      loading: false
    }, {
      color: pieColor,
      description: 'Latency',
      stats: '--',
      average: '--',
      unit: 'ms',
      icon: 'sync',
      loading: false
    }
    ];

    function getRandomArbitrary(min, max) {
      return Math.random() * (max - min) + min;
    }

    function loadPieCharts() {
      $('.chart').each(function () {
        var chart = $(this);
        chart.easyPieChart({
          easing: 'easeOutBounce',
          onStep: function (from, to, percent) {
            $(this.el).find('.percent').text(Math.round(percent));
          },
          barColor: chart.attr('rel'),
          trackColor: 'rgba(0,0,0,0)',
          size: 84,
          scaleLength: 0,
          animation: 2000,
          lineWidth: 9,
          lineCap: 'round',
        });
      });

      $('.refresh-data').on('click', function () {
        updatePieCharts();
      });
    }

    function updatePieCharts() {
      $('.pie-charts .chart').each(function(index, chart) {
        $(chart).data('easyPieChart').update(getRandomArbitrary(55, 90));
      });
    }
    
    function updateSpeedData(status, data) {
    	// Broadcast status (-1: Begin , 0: Downloading, 1: Uploading, 2: End)
    	console.log('method updateSpeedData, status: ' + status);
    	// Downloading
    	if (status == 0) {
    		$scope.charts[0].loading = true;
    		$scope.charts[1].loading = false;
    		$scope.charts[2].loading = true;
    		
    		// Reset chart
    		$scope.charts[0].stats = '--';
    		$scope.charts[0].average = '--';
    		$scope.charts[1].stats = '--';
    		$scope.charts[1].average = '--';
    		$scope.charts[2].stats = '--';
    		$scope.charts[2].average = '--';
    	} 
    	// Uploading
    	else if (status == 1) {
    		$scope.charts[0].loading = false;
    		$scope.charts[1].loading = true;
    		$scope.charts[2].loading = true;
    		
    		// Calculate value download
    		var maxDownload = Math.max.apply( Math, data );
    		maxDownload = maxDownload.toFixed(2);
    		var aveDownload = calAverage(data, 2);
    		$scope.charts[0].stats = maxDownload;
    		$scope.charts[0].average = aveDownload;
    	} 
    	// End
    	else if (status == 2) {
    		$scope.charts[0].loading = false;
    		$scope.charts[1].loading = false;
    		$scope.charts[2].loading = false;
    		// Calculate value latency
    		var latencyArr = data.latency;
    		var minLatency = Math.min.apply( Math, latencyArr );
    		var aveLatency = calAverage(latencyArr, 0);
    		$scope.charts[2].stats = minLatency;
    		$scope.charts[2].average = aveLatency;
    		// Calculate value upload
    		var maxUpload = Math.max.apply( Math, data.upload );
    		maxUpload = maxUpload.toFixed(2);
    		var aveUpload = calAverage(data.upload, 2);
    		$scope.charts[1].stats = maxUpload;
    		$scope.charts[1].average = aveUpload;
    	}
    }
    
    function calAverage(values, roundNumber) {
    	  // Initialise sum with a number value
    	  var sum = 0;
    	  for (var i=0, iLen=values.length; i<iLen; i++) {
    	    sum += +values[i];
    	  }
    	  // Just return the result of the calculation
    	  return (sum / values.length).toFixed(roundNumber);
    	}
    
    $scope.openInfoModal = function (size) {
	      var uibModalInstance = $uibModal.open({
	        animation: $scope.animationsEnabled,
	        templateUrl: 'app/pages/dashboard/infoModal.html',
	        controller: 'InfoModalCtrl',
	        size: size,
	        resolve: {
	          items: function () {
	            return $scope.items;
	          }
	        }
	      });
	
	      uibModalInstance.result.then(function (selectedItem) {
	    	  $scope.selected = selectedItem;
	      }, function () {
	    	  console.log('Modal dismissed at: ' + new Date());
	      });
  };

    $timeout(function () {
    	loadPieCharts();
    	updatePieCharts();
    }, 1000);
    
    $scope.$on('handleBroadcast', function() {
        $scope.message = sharedService.message;
        //console.log('handleBroadcast on DashboardPieChartCtrl, message :' + $scope.message);
        updateSpeedData(sharedService.status, sharedService.measureData);
    });
    
    (function initController() {
    	CommonService.getLocation(function (response) {
    		$scope.geoLocation = response;
        });
	})();
  }
})();