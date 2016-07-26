/**
 * @author v.lugovsky
 * created on 16.12.2015
 */
(function () {
  'use strict';

  angular.module('BlurAdmin.pages.home')
    .controller('HomePageCtrl', ['$scope', '$rootScope', '$location', 'AuthService', HomePageCtrl]);

  /** @ngInject */
  function HomePageCtrl($scope, $rootScope, $location, AuthService) {
	  	var home = this;

	  	home.logout = function () {
	  		console.log('received the logout event for user: '+$scope.currentUser.email);
	        AuthService.clearCredentials();
	        $location.path('/');
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
