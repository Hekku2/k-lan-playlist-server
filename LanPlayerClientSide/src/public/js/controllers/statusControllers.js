var statusControllers = angular.module('statusControllers', ['clientSideLanPlayer.config']);

statusControllers.controller('StatusCtrl', ['$scope', '$http', 'appConfig',
    function ($scope, $http, appConfig) {
        $scope.services = [];

        $http.get(appConfig.services.UserService + 'status').success(function(data) {
            $scope.services.push({
                name: 'UserService',
                status: data.status,
                version: data.version
            });
        });

        $http.get(appConfig.services.PlayerService + 'status').success(function(data) {
            $scope.services.push({
                name: 'PlayerService',
                status: data.status,
                version: data.version
            });
        });
    }
]);