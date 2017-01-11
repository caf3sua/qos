/**
 * @author v.lugovsky
 * created on 16.12.2015
 */
(function () {
  'use strict';

angular.module('BlurAdmin.pages.history')
.controller('HistoryDetailModalCtrl', HistoryDetailModalCtrl);

/** @ngInject */
function HistoryDetailModalCtrl($rootScope, $scope, $uibModalInstance, item) {
	  $scope.item = item;
	  $scope.unitRateDisplay = $rootScope.bitrateType.name;
	
	  $scope.ok = function () {
		  $uibModalInstance.close(item);
	  };
	
	  $scope.cancel = function () {
		  $uibModalInstance.dismiss('cancel');
	  };
};

  
angular.module('BlurAdmin.pages.history')
  .controller('ConfirmHistoryModalCtrl', ConfirmHistoryModalCtrl);

/** @ngInject */
function ConfirmHistoryModalCtrl($scope, $uibModalInstance, historyId) {
	  $scope.historyId = historyId;
	
	  $scope.ok = function () {
		  console.log('Confirmation delete: Delete');
		  $uibModalInstance.close(historyId);
	  };
	
	  $scope.cancel = function () {
		  console.log('Confirmation delete: cancel');
		  $uibModalInstance.dismiss('cancel');
	  };
};


  angular.module('BlurAdmin.pages.history')
      .controller('HistoryPageCtrl', HistoryPageCtrl);

  /** @ngInject */
  function HistoryPageCtrl($rootScope, $scope, $location, $filter, $timeout, $uibModal, toastr, toastrConfig, CommonService) {
	  	var his = this;
	  	
	  	$scope.dataTablePageSize = 10;
	  	
	  	$scope.dataTable = [];
	  	$scope.dataTableInit = [];
	  	
	  	$scope.graphHistoryConfig = {};
	  	
	  	var MAX_GRAPH_ITEM = 5;
	  	// Toast
	  	$scope.quotes = toastQuotesHistory;
	  	$scope.options = toastConfig;
	  	var openedToasts = [];
	  	
	  	
	  	// Display rate
	  	$scope.rateUnit;
	  	$scope.rateUnitDisplay;
	  	
	  	$scope.openHistoryDetailModal = function (item) {
		      var uibModalInstance = $uibModal.open({
		        animation: $scope.animationsEnabled,
		        templateUrl: 'app/pages/history/historyDetailModal.html',
		        controller: 'HistoryDetailModalCtrl',
		        //size: 'lg',
		        resolve: {
		        	item: item
		        }
		      });
		
		      uibModalInstance.result.then(function (selectedItem) {
		    	  $scope.selected = selectedItem;
		      }, function () {
		    	  console.log('Modal dismissed at: ' + new Date());
		      });
	  	};
	  
	  	// Open dialog
	    $scope.openConfirm = function (historyId) {
		      var modalInstance = $uibModal.open({
		        animation: true,
		        templateUrl: 'confirmContentModal.html',
		        controller: 'ConfirmHistoryModalCtrl',
		        size: 'sm', // 'lg' 'sm'
		        resolve: {
		        	historyId: historyId
		        }
		      });
	
		      modalInstance.result.then(function (historyId) {
		    	  $scope.deleteHistory(historyId);
		      }, function () {
		    	  console.log('Modal dismissed at: ' + new Date());
		      });
	    };
	    
	    $scope.deleteHistory = function (historyId) {
	    	console.log("deleteHistory, ID: " + historyId);
	    	CommonService.deleteHistory(historyId, function(result) {
	    		console.log('Delete response');
	    		console.log(result);
	    		// Reload if success
	    		if (result.data.code == 200) {
	    			$scope.getHistoryData();
	    			// Toast success
	    			$scope.openToast('success');
	    		} else {
	    			// Toast error
	    			$scope.openToast('error');
	    		}
	    	});
	    };
	    
	    $scope.openToast = function (type) {
		      angular.extend(toastrConfig, $scope.options);
		      // Info
		      if (type == 'success') {
		    	  var toastType = 'info';
			      var toastQuote = $scope.quotes[0];
		      } else if (type == 'error') {
		    	  var toastType = 'error';
			      var toastQuote = $scope.quotes[1];
		      } else if (type == 'error-search') {
		    	  var toastType = 'error';
			      var toastQuote = $scope.quotes[2];
		      } else if (type == 'error-search-warning') {
		    	  var toastType = 'warning';
			      var toastQuote = $scope.quotes[3];
		      }
		      
		      openedToasts.push(toastr[toastType](toastQuote.message, toastQuote.title, toastQuote.options));
		      $scope.optionsStr = "toastr." + toastType + "(\'" + toastQuote.message + "\', \'" + toastQuote.title + "', " + JSON.stringify(toastQuote.options || {}, null, 2) + ")";
	    };
	  
	    $scope.getHistoryData = function () {
    		// Append unit rate for chart
	    	if ($scope.rateUnit == 0) {
	    		$scope.graphHistoryConfig = graphHistoryConfigKbps;
	    	} else {
	    		$scope.graphHistoryConfig = graphHistoryConfigMbps;
	    	}
	    	
	    	$scope.graphHistoryConfig.series[0].data = [];
    		$scope.graphHistoryConfig.series[1].data = [];
    		$scope.graphHistoryConfig.series[2].data = [];
    		
    		// Check user login
    		if (!$rootScope.globals.currentUser) {
    			return;
    		}
    		
	    	// call server to get history data
	    	CommonService.getHistory(function (result) {
	    		var data = result.data;
	    		
	    		if (data.code == 200) {
	    			$scope.dataTable = data.result;
	    			for (var i = 0; i < $scope.dataTable.length; i++) {
//	    				if (i >= MAX_GRAPH_ITEM) {
//	    					break;
//	    				}
	    				// rowid
	    				$scope.dataTable[i].rowId = i + 1;
	    				
	    				// Convert data
	    				// Kbps
    					converRateDisplayData($scope.dataTable[i], $scope.rateUnit);
	    				
	    				$scope.graphHistoryConfig.series[1].data.push($scope.dataTable[i].downloadSpeed);
	    				$scope.graphHistoryConfig.series[2].data.push($scope.dataTable[i].uploadSpeed);
	    				$scope.graphHistoryConfig.series[0].data.push($scope.dataTable[i].latency);
	    				
	    				var date = $filter('date')(new Date($scope.dataTable[i].startTime), 'dd/MM/yyyy hh:mm:ss');
	    				$scope.graphHistoryConfig.options.xAxis[0].categories.push(date);
					}
	    		} else if (data.code == 404) {
	    			$scope.graphHistoryConfig.series[0].data = [];
	        		$scope.graphHistoryConfig.series[1].data = [];
	        		$scope.graphHistoryConfig.series[2].data = [];
	        		$scope.dataTable = [];
	        	  	$scope.dataTableInit = [];
	    			// Toast error
	    			$scope.openToast('error-search-warning');
	    		} else {
	    			console.log('Get history fail: ' + data.code);
	    			// Toast error
	    			$scope.openToast('error-search');
	    		}
	    	});
	    };
	    
	    function converRateDisplayData (obj, rateUnit) {
	    	var rate = 0;
	    	if (rateUnit == 0) {
	    		rate = 1000;
	    	} else {
	    		rate = 1000000;
	    	}
	    	obj.downloadSpeed = obj.downloadSpeed / rate;
	    	obj.uploadSpeed = obj.uploadSpeed / rate;
    		obj.maxDownloadSpeed = obj.maxDownloadSpeed / rate;
    		obj.maxUploadSpeed = obj.maxUploadSpeed / rate;
    		
    		// round
    		// Kbps
    		if (rateUnit == 0) {
    			obj.downloadSpeed.toFixed(0);
    			obj.uploadSpeed.toFixed(0);
    			obj.maxDownloadSpeed.toFixed(0);
    			obj.maxUploadSpeed.toFixed(0);
    		} else {
    			obj.downloadSpeed.toFixed(2);
    			obj.uploadSpeed.toFixed(2);
    			obj.maxDownloadSpeed.toFixed(2);
    			obj.maxUploadSpeed.toFixed(2);
    		}
	    }
	    
	    (function initController() {
		  	$scope.rateUnit = $rootScope.bitrateType.id;
		  	$scope.rateUnitDisplay = $rootScope.bitrateType.name;
	    	
	    	$timeout(function () {
	    		// Get data from server
	    		$scope.getHistoryData();
	    	}, 500);
	    })();
  }

})();
