/**
 * Created by NamNH on 6/25/2016.
 * Referred: https://github.com/mpetersen/aes-example
 */

'use strict';
angular.module('BlurAdmin.theme')
    .service('AppService', ['Base64', '$http', '$cookieStore', '$rootScope', '$timeout', 'BackendCfg',
        function (Base64, $http, $cookieStore, $rootScope, $timeout, BackendCfg) {
            var service = this;
            service.measureSpeed = function (callback) {
                BackendCfg.setupHttp($http);
                var user = {};
                var json = {};
                //json.inc = Math.round((Math.random() - 0.5) * 20);
                //$http.post(BackendCfg.url+'/api/user/authenticate', user )
                $http.post(BackendCfg.url+'/api/file/speed', json)
                    .success(function (response) {
                        callback(response);
                    });
                console.log('speed event posted...')
            };

            return service;
        }])
    .factory('AppUtil', function () {

        return {
            max: function (input) {
            	var maxValue = Math.max.apply( Math, input );
            	return maxValue.toFixed(2);
            },

            min: function (input) {
            	var minValue = Math.min.apply( Math, input );
            	return minValue.toFixed(2);
            },
            
            average: function (data, roundNumber) {
            	// Initialise sum with a number value
            	var sum = 0;
	          	for (var i=0, iLen=data.length; i<iLen; i++) {
	          	    sum += +data[i];
	          	}
	          	// Just return the result of the calculation
	          	return (sum / data.length).toFixed(roundNumber);
            },
        };
    });
    