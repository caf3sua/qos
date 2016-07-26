/**
 * @author a.demeshko
 * created on 21.01.2016
 */
(function () {
  'use strict';

  angular.module('BlurAdmin.pages.dashboard')
  .controller('MapModalCtrl', ['$scope', '$timeout', '$uibModalInstance', 'NgMap', MapModalCtrl]);


  /** @ngInject */
  function MapModalCtrl($scope, $timeout, $uibModalInstance, NgMap) {
    $scope.link = '';
    $scope.ok = function () {
      $uibModalInstance.close($scope.link);
    };
  }

})();