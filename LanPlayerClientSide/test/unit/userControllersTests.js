describe("UserListCtrl", function() {
    beforeEach(module('userControllers'));

    var $httpBackend,
        $rootScope,
        createController;


    beforeEach(inject(function($injector) {
        $httpBackend = $injector.get('$httpBackend');
        $rootScope = $injector.get('$rootScope');
        var $controller = $injector.get('$controller');

        createController = function() {
            return $controller('UserListCtrl', {'$scope' : $rootScope });
        };
    }));


    it('users contains list of users', function() {
        $httpBackend.expectGET('http://localhost:8081/api/users').respond(200, [
            {
                id: 1,
                username: 'test1'
            },
            {
                id: 2,
                username: 'test2'
            }
        ]);
        var controller = createController();
        $httpBackend.flush();
        expect($rootScope.users).toEqual([
            {
                id: 1,
                username: 'test1'
            },
            {
                id: 2,
                username: 'test2'
            }
        ]);
    });
});