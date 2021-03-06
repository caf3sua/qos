/**
 * Created by NamNH on 6/25/2016.
 * Referred: https://github.com/mpetersen/aes-example
 */

'use strict';
angular.module('BlurAdmin.theme')
    .service('CommonService', ['$http', '$cookieStore', '$rootScope', '$timeout', 'BackendCfg', 'AppUtil', 'Upload',
        function ($http, $cookieStore, $rootScope, $timeout, BackendCfg, AppUtil, Upload) {
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
        		$http.get(BackendCfg.url+'/api/server/getAllServerInfo').then(function(result) {
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
                
        		console.log("Ping url:" + URL);
        		$http.get(URL + '?rnd=' + (new Date().getTime())).then(function(result) {
        			responseTime = (new Date().getTime()) - start;
        			callback(responseTime);
        		}, function(e) {
        			console.log("error");
        		});
            }; 
            
            service.createHistory = function (data, callback) {
//                BackendCfg.setupHttp($http);
                // Get data from global
                var user = $rootScope.globals.currentUser;
                var selectedServer = $rootScope.selectedServer;
                var geoLocation = $rootScope.geoLocation;

                var downloadData = data.download;
                var uploadData = data.upload;
                var latencyData = data.latency;
                // 
                var history = {};
                history.applicationType = "FTTH";
                history.serverName = selectedServer.name;
                history.ips = geoLocation.isp;
                
                history.startTime = data.startTime;
                history.endTime = data.endTime;
                history.duration = data.endTime - data.startTime;
                
                history.ipAddress = geoLocation.query;
                history.userName = user.username;
                history.departmentZone = null;
                history.province = null;
                history.district = null;
                history.address = null;
                history.downloadSpeed = AppUtil.average(downloadData, 2);
                history.maxDownloadSpeed = AppUtil.max(downloadData);
                history.uploadSpeed = AppUtil.average(uploadData, 2);
                history.maxUploadSpeed = AppUtil.max(uploadData);
                // TODO
                history.packageLoss = 10;
                history.latency = AppUtil.average(latencyData, 0);
                history.minLatency = AppUtil.min(latencyData);
                // TODO
                history.variationLatency = 20;
                history.latitudes = geoLocation.lat;
                history.longitudes = geoLocation.lon;
                history.mcc = null;
                history.mnc = null;
                history.networkTechnology = "LTE";
                history.cid = null;
                history.lac = null;
                history.signalStrength = "dB";
                // Extend
                history.ipsCountryCode = geoLocation.countryCode;
                history.serverCountryCode = selectedServer.countryCode;

                $http.post(BackendCfg.url+'/api/history/create', history ).then(function(result) {
        			callback(result);
        		}, function(e) {
        			console.log("error");
        		});
            };
            
            service.deleteHistory = function (id, callback) {
//            	BackendCfg.setupHttp($http);
        		
        		console.log("Delete" + URL);
        		$http.delete(BackendCfg.url+'/api/history/delete/' + id).then(function(result) {
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

                $http.get(BackendCfg.url+'/api/history/getByUsername/' + user.username).then(function(result) {
        			callback(result);
        		}, function(e) {
        			console.log("error");
        		});
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
    