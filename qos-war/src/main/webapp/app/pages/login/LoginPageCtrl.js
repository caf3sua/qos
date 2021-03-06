/**
 * @author v.lugovsky
 * created on 16.12.2015
 */
(function () {
  'use strict';

  angular.module('BlurAdmin.pages.login')
    .controller('LoginPageCtrl', ['$scope', '$rootScope', '$location', 'AuthService', LoginPageCtrl]);

  /** @ngInject */
  function LoginPageCtrl($scope, $rootScope, $location, AuthService) {
	  	var lc = this;

	    (function initController() {
	        // reset login status
	        AuthService.clearCredentials();
	    })();
	
	    lc.login = function () {
	        console.log('received the login event for user: '+lc.user.username);
	        lc.dataLoading = true;
	        $rootScope.isSubmitted = true;
	        AuthService.login(lc.user.username, lc.user.password, function (response) {
	            if (response.code==200) {
	                AuthService.createJWTToken(response.result.user, response.result.token);
	                AuthService.setCredentials();
	                lc.dataLoading = false;
	                $rootScope.isSubmitted = false;
	                $location.path('/home');
	            } else {
	                lc.error = response.result;
	                lc.details = response.details;
	                lc.dataLoading = false;
	                $rootScope.isSubmitted = false;
	            }
	        });
	    };
  }
})();
