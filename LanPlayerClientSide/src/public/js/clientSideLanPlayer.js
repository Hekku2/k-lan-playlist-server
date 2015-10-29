var clientSide = angular.module('clientSideLanPlayer', [
    'ngRoute',
    'userControllers'
]);

clientSide.config(['$routeProvider', '$locationProvider',
    function($routeProvider, $locationProvider) {
        $routeProvider.
            when('/users', {}).
            when('/user/:userId', {}).
            otherwise({
                redirectTo: '/users'
            });
        $locationProvider.html5Mode(true);
    }]);