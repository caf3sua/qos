'use strict';

var BlurAdmin = angular.module('BlurAdmin', [
  'ngAnimate',
  'ui.bootstrap',
  'ui.sortable',
  'ui.router',
  'ngTouch',
  'toastr',
  'smart-table',
  "xeditable",
  'ui.slimscroll',
//  'ngFileUpload',
//  'ngJsTree',
//  'angular-progress-button-styles',
  'ngCookies',
//  'googlechart',
//  'adaptv.adaptStrap',
  'highcharts-ng',
  'ng.deviceDetector',
  'ngMap',
  
  'BlurAdmin.theme',
  'BlurAdmin.pages',
  'BlurAdmin.common'
]);

BlurAdmin.run(['$rootScope', '$timeout', '$location', '$cookieStore', '$http', 'baSidebarService',
   function ($rootScope, $timeout, $location, $cookieStore, $http, baSidebarService) {
       // keep user logged in after page refresh
       $rootScope.globals = $cookieStore.get('globals') || {};
       if ($rootScope.globals.currentUser) {
           $http.defaults.headers.common['Authorization'] = 'Bearer ' + $rootScope.globals.token;
           $rootScope.currentUser = $rootScope.globals.currentUser;
       }
       // Selected server and bit rate
       $rootScope.bitrateType = $cookieStore.get('bitrateType');
       if ($rootScope.bitrateType == undefined) {
    	   //default Mbps
		   $rootScope.bitrateType = {
				  	id: 1,
			        name: 'Mbps',
			        href: '',
			        enable: false,
			        icon: 'socicon-stackoverflow',
			        description: 'mebibytes per second',
			        note: 'n'
		      };
       }

       // init selectedServer
       $rootScope.selectedServer = $cookieStore.get('selectedServer');
       if ($rootScope.selectedServer == undefined) {
		  // default server
		  $rootScope.selectedServer = {serverId : 0, name : "[PE] Hanoi - 1 Gb/s - Bitel", ipAddress: "127.0.0.1", priority: 0, status: "online", url : "http://localhost:8080/qos"};
       }
       
       $rootScope.isSubmitted = false;

       $rootScope.$on('$locationChangeStart', function (event, next, current) {
           console.log('received event: ' + event + ' from: ' + current + ' to go to next: ' + next);
           // redirect to login page if not logged in and trying to access a restricted page
           if (!baSidebarService.isMenuCollapsed()) {
        	   $timeout(function () {
        		   baSidebarService.setMenuCollapsed(true);
        	   }, 10);
    	   }
           
           var restrictedPage = $.inArray($location.path(), ['/login', '/register', '/admin.login', '/adm.register', '/admin', '/app', '/dashboard', '/file', '/speed', '/about', '/setting']) === -1;
           var loggedIn = $rootScope.globals.currentUser;
           $rootScope.currentUser = $rootScope.globals.currentUser;
	           		if (restrictedPage && !loggedIn) {
	//               if($location.path().indexOf('admin') > -1) {
	//                   $location.path('/admin.login');
	//               } else if($location.path().indexOf('app') > -1) {
	//                   $location.path('/login');
	//               } else
	       			if($location.path().indexOf('history') > -1) {
	                   $location.path('/login');
	                   $scope.$apply();
	       			} else {
	                   $location.path('/home');
	       			}
           		}
           });
       }
   ]);