/**
 * @author v.lugovsky
 * created on 16.12.2015
 */
(function () {
  'use strict';

  angular.module('BlurAdmin.pages', [
    'ui.router',

    'BlurAdmin.pages.home',
    'BlurAdmin.pages.dashboard',
    'BlurAdmin.pages.file',
//    'BlurAdmin.pages.ui',
//    'BlurAdmin.pages.components',
//    'BlurAdmin.pages.tables',
    'BlurAdmin.pages.history',
//    'BlurAdmin.pages.maps',
    'BlurAdmin.pages.setting',
    'BlurAdmin.pages.about',
    
    // No display on sidebar
    'BlurAdmin.pages.login',
    'BlurAdmin.pages.register',
//    'BlurAdmin.pages.form',
//    'BlurAdmin.pages.charts',
  ])
      .config(routeConfig)
      .config(['$translateProvider', function($translateProvider){
	    // Register a loader for the static files
	    // So, the module will search missing translation tables under the specified urls.
	    // Those urls are [prefix][langKey][suffix].
	    $translateProvider.useStaticFilesLoader({
	      prefix: 'assets/i18n/',
	      suffix: '.json'
	    });
	    // Tell the module what language to use by default, vi_VN
	    $translateProvider.preferredLanguage('en');
	    // Tell the module to store the language in the local storage
	    $translateProvider.useLocalStorage();
	  }]);

  /** @ngInject */
  function routeConfig($urlRouterProvider, baSidebarServiceProvider) {
    $urlRouterProvider.otherwise('/dashboard');

//    baSidebarServiceProvider.addStaticItem({
//      title: 'Pages',
//      icon: 'ion-document',
//      subMenu: [{
//        title: 'Sign In',
//        fixedHref: 'auth.html',
//        blank: true
//      }, {
//        title: 'Sign Up',
//        fixedHref: 'reg.html',
//        blank: true
//      }, {
//        title: 'User Profile',
//        stateRef: 'profile'
//      }, {
//        title: '404 Page',
//        fixedHref: '404.html',
//        blank: true
//      }]
//    });
//    baSidebarServiceProvider.addStaticItem({
//      title: 'Menu Level 1',
//      icon: 'ion-ios-more',
//      subMenu: [{
//        title: 'Menu Level 1.1',
//        disabled: true
//      }, {
//        title: 'Menu Level 1.2',
//        subMenu: [{
//          title: 'Menu Level 1.2.1',
//          disabled: true
//        }]
//      }]
//    });
  }

})();
