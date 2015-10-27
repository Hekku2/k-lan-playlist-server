describe("UserListCtrl", function() {
    beforeEach(module('userControllers'));

    var $httpBackend;
    var $rootScope;
    var createController;
    var appConfig;

    var userCreator = function(id){
        return {
            id: id,
            username: 'test' + id
        };
    };

    beforeEach(inject(function($injector) {
        $httpBackend = $injector.get('$httpBackend');
        $rootScope = $injector.get('$rootScope');
        var $controller = $injector.get('$controller');
        appConfig = $injector.get('appConfig');

        createController = function() {
            return $controller('UserListCtrl', {'$scope' : $rootScope });
        };
    }));

    it('users contains list of users', function() {
        $httpBackend.expectGET(appConfig.services.UserService + 'users').respond(200, [
            userCreator(1),
            userCreator(2)
        ]);
        createController();
        $httpBackend.flush();
        expect($rootScope.users).toEqual([
            userCreator(1),
            userCreator(2)
        ]);
    });
});