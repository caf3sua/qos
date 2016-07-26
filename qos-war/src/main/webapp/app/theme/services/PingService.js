/**
 * Created by NamNH on 6/25/2016.
 * Referred: https://github.com/mpetersen/aes-example
 */

'use strict';
angular.module('BlurAdmin.theme')
    .service('PingService', ['$http', '$cookieStore', '$rootScope', '$timeout', 'BackendCfg',
        function ($http, $cookieStore, $rootScope, $timeout, BackendCfg) {
            var service = this;
            
            service.ping = function (URL, callback) {
            	$http.defaults.headers.common['Access-Control-Allow-Origin'] = '*';
            	delete $http.defaults.headers.common['X-Requested-With'];
        		delete $http.defaults.headers.common.Authorization;
        		
        		var responseTime = 0;
                var start = (new Date()).getTime();
                
        		console.log("Ping url:" + URL);
        		$http.get(URL + '?rnd=' + (new Date().getTime())).then(function(result) {
        			responseTime = (new Date().getTime()) - start;
        			callback(responseTime);
        		}, function(e) {
        			console.log("error");
        		});
            }; 
            
            return service;

    }]);
    