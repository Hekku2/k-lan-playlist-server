var clientSide = angular.module('clientSideLanPlayer', [
    'ngRoute',
    'userControllers',
    'statusControllers'
]);

clientSide.config(['$routeProvider', '$locationProvider',
    function($routeProvider, $locationProvider) {
        $locationProvider.html5Mode(true);
        $routeProvider.
            when('/user/:userId', {}).
            otherwise({
                redirectTo: '/'
            });
    }
]);