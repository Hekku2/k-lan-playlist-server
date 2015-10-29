var userControllers = angular.module('userControllers', ['clientSideLanPlayer.config']);

userControllers.controller('UserListCtrl', ['$scope', '$http', 'appConfig',
    function ($scope, $http, appConfig) {
        $scope.users = [];

        $http.get(appConfig.services.UserService + 'users').success(function(data) {
            $scope.users = data;
        });
    }
]);

userControllers.controller('UserDetailsCtrl', ['$scope', '$http', '$route', '$routeParams', 'appConfig',
    function ($scope, $http, $route, $routeParams, appConfig) {
        $scope.user = {};
        $scope.$on('$routeChangeSuccess', function() {
            $http.get(appConfig.services.UserService + 'user/' + $routeParams.userId).error(function(err){
                $scope.user = {};
            }).success(function(data) {
                $scope.user = data;
            });
        });
    }
]);