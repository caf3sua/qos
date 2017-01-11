/**
 * Created by NamNH on 6/25/2016.
 * Referred: https://github.com/mpetersen/aes-example
 */

'use strict';
angular.module('BlurAdmin.theme')
    .service('CommonService', ['$http', '$cookieStore', '$rootScope', '$timeout', 'BackendCfg', 'AppUtil', '$cordovaNetwork',
        function ($http, $cookieStore, $rootScope, $timeout, BackendCfg, AppUtil, $cordovaNetwork) {
            var service = this;
            
            service.getLocation = function (callback) {
//            	delete $http.defaults.headers.common['Access-Control-Allow-Origin'];
//            	delete $http.defaults.headers.common['X-Requested-With'];
//        		delete $http.defaults.headers.common.Authorization;
        		
        		var json = 'http://ip-api.com/json';
        		$.getJSON( json )
        		  	.done(function( result ) {
        		  		callback(result);
        		  	})
        		  	.fail(function( jqxhr, textStatus, error ) {
        		  		var err = textStatus + ", " + error;
        		  		console.log( "Request ip-api.com Failed: " + err );
        		});
            };
            
            service.getServerInfo = function (callback) {
//            	delete $http.defaults.headers.common['Access-Control-Allow-Origin'];
//            	delete $http.defaults.headers.common['X-Requested-With'];
//        		delete $http.defaults.headers.common.Authorization;
                
        		console.log("getServerInfo");
        		$http.get(BackendCfg.contextPath(location) + '/api/server/getAllServerInfo').then(function(result) {
        			console.log(result);
        			console.log(result.data.result);
        			callback(result.data.result);
        		}, function(e) {
        			console.log("error");
        		});
            };
            
            service.ping = function (URL, callback) {
//            	delete $http.defaults.headers.common['Access-Control-Allow-Origin'];
//            	delete $http.defaults.headers.common['X-Requested-With'];
//        		delete $http.defaults.headers.common.Authorization;
        		
        		var responseTime = 0;
                var start = (new Date()).getTime();
                
//        		$http.get(URL + '?rnd=' + (new Date().getTime())).then(function(result) {
//        			responseTime = (new Date().getTime()) - start;
//        			// ping : router from source -> destination
//        			responseTime = parseInt(responseTime / 2);
//        			callback(responseTime);
//        		}, function(e) {
//        			responseTime = -1;
//        			callback(responseTime);
//        			console.log("error when ping");
//        		});
        		
        		$.getJSON(URL + '?rnd=' + (new Date().getTime()))
    		  	.done(function( result ) {
    		  		responseTime = (new Date().getTime()) - start;
        			// ping : router from source -> destination
        			responseTime = parseInt(responseTime / 2);
        			callback(responseTime);
    		  	})
    		  	.fail(function( jqxhr, textStatus, error ) {
    		  		responseTime = -1;
        			callback(responseTime);
        			console.log("error when ping");
    		  	});
            }; 
            
            service.createHistory = function (data, callback) {
            	var type = '';
            	try {
            		type = $cordovaNetwork.getNetwork(); 
            	} catch (e) {
            		type = 'UNKNOWN';
            	}
                // Get data from global
                var user = $rootScope.globals.currentUser;
                var selectedServer = $rootScope.selectedServer;
                var geoLocation = $rootScope.geoLocation;

                var downloadData = data.download;
                var uploadData = data.upload;
                var latencyData = data.latency;
                // 
                var history = {};
                history.applicationType = "Website";
                history.serverName = selectedServer.name;
                history.isp = geoLocation.isp;
                
                history.startTime = data.startTime;
                history.endTime = data.endTime;
                history.duration = data.endTime - data.startTime;
                
                history.ipAddress = geoLocation.query;
                history.userName = user.username;
                history.departmentZone = null;
                history.province = null;
                history.district = null;
                history.address = null;
                
                if (data.unitType == 0) {
                	history.downloadSpeed = AppUtil.averagePoint(downloadData, 2);
                	history.downloadSpeed = (history.downloadSpeed / MBPS_TO_KBPS).toFixed(2);
                	
                    history.maxDownloadSpeed = AppUtil.maxPoint(downloadData);
                    history.maxDownloadSpeed = (history.maxDownloadSpeed / MBPS_TO_KBPS).toFixed(2);
                    
                    history.uploadSpeed = AppUtil.averagePoint(uploadData, 2);
                    history.uploadSpeed = (history.uploadSpeed / MBPS_TO_KBPS).toFixed(2);
                    
                    history.maxUploadSpeed = AppUtil.maxPoint(uploadData);
                    history.maxUploadSpeed = (history.maxUploadSpeed / MBPS_TO_KBPS).toFixed(2);
                    
                    // Convert to bit per second
                    history.downloadSpeed = history.downloadSpeed * 1000;
                    history.maxDownloadSpeed = history.maxDownloadSpeed * 1000;
                    history.uploadSpeed = history.uploadSpeed * 1000;
                    history.maxUploadSpeed = history.maxUploadSpeed * 1000;
                } else {
                	history.downloadSpeed = AppUtil.averagePoint(downloadData, 2);
                    history.maxDownloadSpeed = AppUtil.maxPoint(downloadData);
                    history.uploadSpeed = AppUtil.averagePoint(uploadData, 2);
                    history.maxUploadSpeed = AppUtil.maxPoint(uploadData);
                    // Convert to bit per second
                    history.downloadSpeed = history.downloadSpeed * 1000000;
                    history.maxDownloadSpeed = history.maxDownloadSpeed * 1000000;
                    history.uploadSpeed = history.uploadSpeed * 1000000;
                    history.maxUploadSpeed = history.maxUploadSpeed * 1000000;
                }

                history.packageLoss = 0;
                history.latency = AppUtil.average(latencyData, 0);
                history.minLatency = AppUtil.min(latencyData);
                var maxLatency = AppUtil.max(latencyData);
                history.variationLatency = maxLatency - history.minLatency;
                history.latitudes = geoLocation.lat;
                history.longitudes = geoLocation.lon;
                history.mcc = null;
                history.mnc = null;
                history.networkTechnology = type;
                history.cid = null;
                history.lac = null;
                history.signalStrengthUnit = "dB";
                history.signalStrength = null;
                // Extend
                history.ispCountryCode = geoLocation.countryCode;
                history.serverCountryCode = selectedServer.countryCode;

                $http.post(BackendCfg.contextPath(location) + '/api/history/create', history ).then(function(result) {
        			callback(result);
        		}, function(e) {
        			console.log("error");
        		});
            };
            
            service.deleteHistory = function (id, callback) {
//            	BackendCfg.setupHttp($http);
        		
        		console.log("Delete" + URL);
        		$http.delete(BackendCfg.contextPath(location) + '/api/history/delete/' + id).then(function(result) {
        			callback(result);
        		}, function(e) {
        			console.log("error");
        		});
            }; 
            
            service.getHistory = function (callback) {
//                BackendCfg.setupHttp($http);
                // Get data from global
                var user = $rootScope.globals.currentUser;
                console.log('get history for username: ' + user.username);

                $http.get(BackendCfg.contextPath(location) + '/api/history/getByUsername/' + user.username).then(function(result) {
        			callback(result);
        		}, function(e) {
        			console.log("error");
        		});
            };
            
            service.getContextPath = function () {
            	var contextPath = $location.absUrl();
            	contextPath = redirectUri.substr(0, redirectUri.indexOf('#'));
            	return contextPath;
            };
            
            return service;

    }])
    .factory('sharedService', function($rootScope) {
        var sharedService = {};

        sharedService.status = '';
        sharedService.message = '';
        sharedService.measureData = {};

        sharedService.prepForBroadcast = function(status, msg, measureData) {
        	this.status = status;
            this.message = msg;
            this.measureData = measureData;
            this.broadcastItem();
        };

        sharedService.broadcastItem = function() {
            $rootScope.$broadcast('handleBroadcast');
        };

        return sharedService;
    });
    