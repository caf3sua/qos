/**
 * Created by NamNH on 6/25/2016.
 */
var commonModule = angular.module('BlurAdmin.common', []);

commonModule.constant('BackendCfg',  {
    url: SERVER_URL,
    setupHttp: function(http) {
        http.defaults.useXDomain = true;
        http.defaults.withCredentials = true;
    },
    contextPath: function(location) {
    	var contextPath = location.href;
    	contextPath = contextPath.substr(0, contextPath.indexOf('#'));
    	return contextPath;
    }
});

var compareTo = function() {
    return {
        require: "ngModel",
        scope: {
            otherModelValue: "=compareTo"
        },
        link: function(scope, element, attributes, ngModel) {

            ngModel.$validators.compareTo = function(modelValue) {
                return modelValue == scope.otherModelValue;
            };

            scope.$watch("otherModelValue", function() {
                ngModel.$validate();
            });
        }
    };
};
commonModule.directive('compareTo', compareTo);
