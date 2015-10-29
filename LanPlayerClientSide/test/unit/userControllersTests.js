describe('userControllers', function() {
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
        appConfig = $injector.get('appConfig');
    }));

    describe('UserListCtrl', function(){
        beforeEach(inject(function($injector) {
            var $controller = $injector.get('$controller');
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

    describe('UserDetailsCtrl', function(){
        beforeEach(inject(function($injector) {
            var $controller = $injector.get('$controller');
            var $route = {};
            var $routeParams = {userId:666};
            createController = function() {
                return $controller('UserDetailsCtrl', {'$scope' : $rootScope, '$route': $route, '$routeParams' : $routeParams});
            };
        }));

        it('user retrieves correct user', function() {
            var expectedUserId = 666;
            $httpBackend.expectGET(appConfig.services.UserService + 'user/' + expectedUserId).respond(200, userCreator(expectedUserId));
            createController();
            $rootScope.$broadcast('$routeChangeSuccess', {});
            $httpBackend.flush();
            var testuser = userCreator(expectedUserId);
            expect($rootScope.user.id).toEqual(testuser.id);
            expect($rootScope.user.username).toEqual(testuser.username);
        });
    });
});