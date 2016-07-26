/**
 * @author v.lugovsky
 * created on 16.12.2015
 */
(function () {
  'use strict';

  angular.module('BlurAdmin.pages.register', [])
      .config(routeConfig);

  /** @ngInject */
  function routeConfig($stateProvider) {
    $stateProvider
        .state('register', {
          url: '/register',
          title: 'Register',
          templateUrl: 'app/pages/register/register.html',
//          controller: 'RegisterPageCtrl',
//          sidebarMeta: {
//              icon: 'ion-gear-b',
//              order: 400,
//            },
        });
  }

})();
