var userControllers = angular.module('userControllers', []);

userControllers.controller('UserListCtrl', ['$scope', '$http',
    function ($scope, $http) {
        $http.get('http://localhost:8081/api/users').success(function(data) {
            $scope.users = data;
        });

        /*
        $scope.users = [
            {
                'username': 'Mock user 1',
                'id': 1
            },
            {
                'username': 'Mock user 2',
                'id': 2
            },
            {
                'username': 'Mock user 3',
                'id': 3
            }
        ];
        */
    }]);