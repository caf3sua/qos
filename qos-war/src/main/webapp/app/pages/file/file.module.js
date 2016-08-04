/**
 * @author v.lugovsky
 * created on 16.12.2015
 */
(function () {
  'use strict';

  angular.module('BlurAdmin.pages.file', [])
      .config(routeConfig);

  /** @ngInject */
  function routeConfig($stateProvider) {
    $stateProvider
        .state('file', {
          url: '/file',
          templateUrl: 'app/pages/file/file.html',
          title: 'Speed App'
        });
  }

})();
