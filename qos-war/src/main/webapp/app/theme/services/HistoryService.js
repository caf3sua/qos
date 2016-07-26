/**
 * Created by NamNH on 6/25/2016.
 * Referred: https://github.com/mpetersen/aes-example
 */

'use strict';
angular.module('BlurAdmin.theme')
    .service('HistoryService', ['$http', '$cookieStore', '$root', '$rootScope', '$timeout', 'BackendCfg', 'CommonUtil',
        function ($http, $cookieStore, $root, $rootScope, $timeout, BackendCfg, CommonUtil) {
            var service = this;
            
            service.createHistory = function (data, callback) {
                BackendCfg.setupHttp($http);
                // Get data from global
                debugger
                var user = $rootScope.globals.user;
                var selectedServer = $rootScope.selectedServer;
                var geoLocation = $rootScope.geoLocation;

                var downloadData = data.download;
                var uploadData = data.upload;
                var latencyData = data.latency;
                // 
                var history = {};
                history.applicationType = "FTTH";
                history.serverName = selectedServer.name;
                history.ips = geoLocation.data.isp;
                history.startTime = new Date();
                history.endTime = new Date();
                history.duration = 20;
                history.ipAddress = geoLocation.data.query;
                history.userName = user.username;
                history.departmentZone = null;
                history.province = null;
                history.district = null;
                history.address = null;
                history.downloadSpeed = 40;
                history.maxDownloadSpeed = CommonUtil.max();
                history.uploadSpeed = 30;
                history.maxUploadSpeed = 30;
                history.packageLoss = 10;
                history.latency = 200;
                history.minLatency = 20;
                history.variationLatency = null;
                history.latitudes = geoLocation.data.lat;
                history.longitudes = geoLocation.data.lon;
                history.mcc = null;
                history.mnc = null;
                history.networkTechnology = "LTE";
                history.cid = null;
                history.lac = null;
                history.signalStrength = "dB";
            	
                history.password = '';
                history.vpassword = '';
                history.iv = aesPack.iv;
                history.salt = aesPack.salt;
                history.keySize = aesPack.keySize;
                history.iterations = aesPack.iterations;
                history.encryptedPassword = aesPack.ciphertext;

                console.log('create history for username: ' + user.username);

                $http.post(BackendCfg.url+'/api/history/create', history )
                    .success(function (response) {
                        callback(response);
                    });
            };
            
            return service;
    }]);