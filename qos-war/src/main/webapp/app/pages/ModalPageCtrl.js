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
