/**
 * @author Nam, Nguyen Hoai
 * created on 16.12.2015
 */
(function () {
  'use strict';


  angular.module('BlurAdmin.pages.setting')
    .controller('SettingPageCtrl', ['$rootScope', '$scope', '$filter', '$uibModal', '$cookieStore', 'toastr', 'toastrConfig', 'CommonService', SettingPageCtrl]);

  /** @ngInject */
  function SettingPageCtrl($rootScope, $scope, $filter, $uibModal, $cookieStore, toastr, toastrConfig, CommonService) {
	  var defaultConfig = angular.copy(toastrConfig);
	  $scope.selectedServer = {};
	  var initLoading = true;
	  
	  $scope.servers = [];
	  
	  $scope.bitrateTypes = BITRATE_TYPES;
	  var openedToasts = [];
	  $scope.options = toastConfig; 

	  
    $scope.unconnect = function (item) {
    	// disable all
    	for (var i = 0; i < $scope.bitrateTypes.size(); i++) {
    		$scope.bitrateTypes[i].enable = false;
    	}
        item.enable = true;
    };
    
    $scope.selectBitrate = function (item) {
    	if (item == null || item == undefined) {
    		return;
    	}
    	// disable all
    	for (var i = 0; i < $scope.bitrateTypes.length; i++) {
    		$scope.bitrateTypes[i].enable = false;
    	}
        item.enable = true;
        //save cookie
        $rootScope.bitrateType = item;
        $cookieStore.put('bitrateType', $rootScope.bitrateType);
        console.log($cookieStore.get('bitrateType'));
        
        $scope.createInfoMsg();
    };
    
    $scope.createInfoMsg = function () {
    	$scope.clearToasts();
        if (!initLoading) {
        	$scope.openToast('success');
        }
    };
    
    $scope.updateServer = function(item) {
        //save cookie
        $rootScope.selectedServer = item;
        $cookieStore.put('selectedServer', $rootScope.selectedServer);
        console.log(item);

    	$scope.createInfoMsg();
    };

    $scope.clearToasts = function () {
      toastr.clear();
    };

    $scope.openToast = function (type) {
    	  angular.extend(toastrConfig, $scope.options);
	      // Info
	      if (type == 'success') {
	    	  var toastType = 'info';
		      var toastQuote = toastQuotesSetting[0];
	      } else if (type == 'error') {
	    	  var toastType = 'error';
		      var toastQuote = toastQuotesSetting[1];
	      }
	      
	      openedToasts.push(toastr[toastType](toastQuote.message, toastQuote.title, toastQuote.options));
	      $scope.optionsStr = "toastr." + toastType + "(\'" + toastQuote.message + "\', \'" + toastQuote.title + "', " + JSON.stringify(toastQuote.options || {}, null, 2) + ")";
    };

    $scope.$on('$destroy', function iVeBeenDismissed() {
      angular.extend(toastrConfig, defaultConfig);
    });
    
    (function initController() {
	      // init bitrate
		  $rootScope.bitrateType = $cookieStore.get('bitrateType');
		  if ($rootScope.bitrateType == undefined) {
			  // default Mbps
			  $rootScope.bitrateType = $scope.bitrateTypes[1];
		  }
		  $scope.selectBitrate($scope.bitrateTypes[$rootScope.bitrateType.id]);
		  // Call api to get info
		  CommonService.getServerInfo(function (response) {
			  console.log(response);
			  $scope.servers = response;
			  // init selectedServer
			  $rootScope.selectedServer = $cookieStore.get('selectedServer');
			  if ($rootScope.selectedServer == undefined) {
				  // default server
				  $rootScope.selectedServer = $scope.servers[0];
			  }
			  $scope.selectedServer = $scope.servers[$rootScope.selectedServer.id]; 
		  });
		  
		  // Update flag
		  initLoading = false;
	})();
  }

})();
