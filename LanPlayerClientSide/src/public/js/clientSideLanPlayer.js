var clientSide = angular.module('clientSideLanPlayer', [
    'ngRoute',
    'userControllers'
]);

clientSide.config(['$routeProvider',
    function($routeProvider) {
        $routeProvider.
            when('/users', {
                controller: 'UserListCtrl'
            }).
            otherwise({
                redirectTo: '/users'
            });
    }]);