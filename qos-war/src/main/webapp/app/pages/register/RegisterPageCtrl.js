/**
 * @author v.lugovsky
 * created on 16.12.2015
 */
(function () {
  'use strict';

  angular.module('BlurAdmin.pages.register')
    .controller('RegisterPageCtrl', ['$location', '$scope', '$rootScope', 'AuthService', RegisterPageCtrl]);
  
  /** @ngInject */
  function RegisterPageCtrl($location, $scope, $rootScope, AuthService) {
	  var rc = this;
	    console.log('register controller');
	    rc.register = function (admin) {
	        console.log('received the register event for user: '+rc.user.username);
	        $rootScope.isSubmitted = true;
	        rc.dataLoading = true;
	        rc.user.admin = admin;
	        AuthService.register(rc.user, function (response) {
	            if (response.code==200) {
	                AuthService.createJWTToken(response.result.user, response.result.token);
	                AuthService.setCredentials();
	                $location.path('/app');
	            } else {
	                rc.error = response.result;
	                rc.details = response.details;
	                rc.dataLoading = false;
	                $rootScope.isSubmitted = false;
	            }
	        });
	    };
  }

})();
