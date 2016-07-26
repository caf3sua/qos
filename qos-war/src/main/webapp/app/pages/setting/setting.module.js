/**
 * @author v.lugovsky
 * created on 16.12.2015
 */
(function () {
  'use strict';

  angular.module('BlurAdmin.pages.setting', [])
      .config(routeConfig);

  /** @ngInject */
  function routeConfig($stateProvider) {
    $stateProvider
        .state('setting', {
          url: '/setting',
          title: 'Settings',
          templateUrl: 'app/pages/setting/setting.html',
          controller: 'SettingPageCtrl',
          sidebarMeta: {
              icon: 'ion-gear-b',
              order: 400,
            },
        });
  }

})();
