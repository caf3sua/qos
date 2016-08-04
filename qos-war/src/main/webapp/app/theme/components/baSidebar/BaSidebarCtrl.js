/**
 * @author Nam, Nguyen Hoai
 * created on 16.12.2015
 */
(function () {
  'use strict';

  angular.module('BlurAdmin.theme.components')
    .controller('BaSidebarCtrl', BaSidebarCtrl);

  /** @ngInject */
  function BaSidebarCtrl($rootScope, $scope, baSidebarService) {

    $scope.menuItems = baSidebarService.getMenuItems();
    $scope.defaultSidebarState = $scope.menuItems[0].stateRef;

    $scope.hoverItem = function ($event) {
      $scope.showHoverElem = true;
      $scope.hoverElemHeight =  $event.currentTarget.clientHeight;
      var menuTopValue = 66;
      $scope.hoverElemTop = $event.currentTarget.getBoundingClientRect().top - menuTopValue;
    };
    
    $scope.isShowItemMenu = function (item) {
        // Check login with history
    	if (item.name == 'history' && !$rootScope.globals.currentUser) {
    		return false;
    	}
    	
    	return true;
      };

    $scope.$on('$stateChangeSuccess', function () {
      if (baSidebarService.canSidebarBeHidden()) {
        baSidebarService.setMenuCollapsed(true);
      }
    });
  }
})();