/**
 * @author v.lugovsky
 * created on 16.12.2015
 */
(function () {
  'use strict';

  angular.module('BlurAdmin.pages.history', [])
    .config(routeConfig);

  /** @ngInject */
  function routeConfig($stateProvider, $urlRouterProvider) {
    $stateProvider
        .state('history', {
          url: '/history',
          templateUrl: 'app/pages/history/history.html',
          title: 'History',
          controller: 'HistoryPageCtrl',
          sidebarMeta: {
        	  icon: 'ion-grid',
              order: 200,
          },
        });
  }

})();
