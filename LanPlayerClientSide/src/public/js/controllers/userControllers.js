var userControllers = angular.module('userControllers', ['clientSideLanPlayer.config']);

userControllers.controller('UserListCtrl', ['$scope', '$http', 'appConfig',
    function ($scope, $http, appConfig) {
        $http.get(appConfig.services.UserService + 'users').success(function(data) {
            $scope.users = data;
        });
    }]);