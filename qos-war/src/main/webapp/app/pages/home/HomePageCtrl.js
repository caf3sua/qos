/**
 * @author v.lugovsky
 * created on 16.12.2015
 */
(function () {
  'use strict';
  
  angular.module('BlurAdmin.pages.home')
  .controller('ConfirmSignoutModalCtrl', ConfirmSignoutModalCtrl);
  
  function ConfirmSignoutModalCtrl($rootScope, $scope, $uibModalInstance, item) {
	  $scope.item = item;
	
	  $scope.ok = function () {
		  $uibModalInstance.close(item);
	  };
	
	  $scope.cancel = function () {
		  $uibModalInstance.dismiss('cancel');
	  };
};

  angular.module('BlurAdmin.pages.home')
    .controller('HomePageCtrl', ['$scope', '$rootScope', '$location', '$uibModal', 'AuthService', HomePageCtrl]);


  /** @ngInject */
  function HomePageCtrl($scope, $rootScope, $location, $uibModal, AuthService) {
	  	var home = this;

	  	home.logout = function () {
	  		console.log('received the logout event for user: '+$scope.currentUser.email);
	        AuthService.clearCredentials();
	        $location.path('/');
	    };
	    
	    // Open dialog
	    home.openConfirmSignout = function () {
		      var modalInstance = $uibModal.open({
		        animation: true,
		        templateUrl: 'confirmSignoutModal.html',
		        controller: 'ConfirmSignoutModalCtrl',
		        size: 'sm', // 'lg' 'sm'
		        resolve: {
		        	//historyId: historyId
		        }
		      });
	
		      modalInstance.result.then(function () {
		    	  console.log('Modal dismissed at: ' + new Date());
		    	  //$scope.deleteHistory(historyId);
		      }, function () {
		    	  console.log('Modal dismissed at: ' + new Date());
		      });
	    };
	    
	    $scope.myInterval = 2000;
	    $scope.noWrapSlides = false;
	    $scope.active = 0;
	    var slides = $scope.slides = [];
	    var currIndex = 0;

	    $scope.initSlide = function() {
	      slides.push({
	        image: 'assets/img/banner/bitel_panner1.png',
	        text: 'Bitel Peru- Telefonía móvil, Internet móvil, telecomunicaciones ...',
	        id: 0
	      });
	      slides.push({
	          image: 'assets/img/banner/bitel_panner2.png',
	          text: 'Bitel Peru- Telefonía móvil, Internet móvil, telecomunicaciones ...',
	          id: 1
	        });
	    };
  }
})();
