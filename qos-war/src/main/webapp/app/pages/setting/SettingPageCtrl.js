/**
 * @author v.lugovsky
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
	  
    $scope.bitrateTypes = [
      {
    	id: 0,
        name: 'Kb/s ',
        href: '',
        enable: false,
        icon: 'socicon-stackoverflow',
        description: 'kibibytes per second',
        note: 'n * 1024'
      },
      {
    	id: 1,
        name: 'Mb/s',
        href: '',
        enable: false,
        icon: 'socicon-stackoverflow',
        description: 'mebibytes per second',
        note: 'n'
      }
    ];

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
        	$scope.openToast();
        }
    };
    
    $scope.updateServer = function(item) {
        //save cookie
        $rootScope.selectedServer = item;
        $cookieStore.put('selectedServer', $rootScope.selectedServer);
        console.log(item);

    	$scope.createInfoMsg();
    };

    var openedToasts = [];
    $scope.options = {
      autoDismiss: false,
      positionClass: 'toast-top-center',
      type: 'info',
      timeOut: '2000',
      extendedTimeOut: '2000',
      allowHtml: false,
      closeButton: false,
      tapToDismiss: true,
      progressBar: true,
      newestOnTop: true,
      maxOpened: 0,
      preventDuplicates: false,
      preventOpenDuplicates: false,
      title: "Well done!",
      msg: "You successfully update setting."
    };

    $scope.clearToasts = function () {
      toastr.clear();
    };

    $scope.openToast = function () {
      angular.extend(toastrConfig, $scope.options);
      openedToasts.push(toastr[$scope.options.type]($scope.options.msg, $scope.options.title));
      var strOptions = {};
      for (var o in  $scope.options) if (o != 'msg' && o != 'title')strOptions[o] = $scope.options[o];
      $scope.optionsStr = "toastr." + $scope.options.type + "(\'" + $scope.options.msg + "\', \'" + $scope.options.title + "\', " + JSON.stringify(strOptions, null, 2) + ")";
    };

    $scope.$on('$destroy', function iVeBeenDismissed() {
      angular.extend(toastrConfig, defaultConfig);
    });
    
    (function initController() {
	      // init bitrate
		  $rootScope.bitrateType = $cookieStore.get('bitrateType');
		  if ($rootScope.bitrateType == undefined) {
			  // default Mb/s
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
				  // default Mb/s
				  $rootScope.selectedServer = $scope.servers[0];
			  }
			  $scope.selectedServer = $scope.servers[$rootScope.selectedServer.serverId]; 
		  });
		  
		  // Update flag
		  initLoading = false;
	})();
  }

})();
