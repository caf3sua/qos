/**
 * @author v.lugovsky
 * created on 16.12.2015
 */
(function () {
  'use strict';

  angular.module('BlurAdmin.pages.login')
    .controller('LoginPageCtrl', ['$scope', '$rootScope', '$location', 'AuthService', '$window', '$state', LoginPageCtrl]);

  /** @ngInject */
  function LoginPageCtrl($scope, $rootScope, $location, AuthService, $window, $state) {
	  	var lc = this;

	    (function initController() {
	    	lc.dataLoading = false;
            $rootScope.isSubmitted = false;
            
	        // reset login status
	        AuthService.clearCredentials();
	    })();
	
	    function allLetter(inputtxt) { 
		      var letters = /^[A-Za-z0-9_]+$/;
		      if(inputtxt.match(letters)) {
		    	  return true;
		      } else {
		    	  return false;
		      }
	    }
	    
	    lc.login = function () {
	        console.log('received the login event for user: '+lc.user.username);
	        lc.dataLoading = true;
	        $rootScope.isSubmitted = true;
	        
	        // validate
	        var result = allLetter(lc.user.username);
	        console.log(result);
	        if (result == false) {
	        	lc.error = 'Username contains special characters';
                lc.details = 'Username contains special characters';
                lc.dataLoading = false;
                $rootScope.isSubmitted = false;
	        	return;
	        }
	        
	        
	        AuthService.login(lc.user.username, lc.user.password, function (response) {
	        	console.log('AuthService.login, username:' + lc.user.username + ", password:" + lc.user.password + ", response code:" + response.code==200);
	            if (response.code==200) {
	                AuthService.createJWTToken(response.result.user, response.result.token);
	                AuthService.setCredentials();
	                lc.dataLoading = false;
	                $rootScope.isSubmitted = false;
	                $location.path('/home');
	                $window.location.href = $location.absUrl();
	                $window.location.reload();
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
