describe('statusControllers', function() {
    beforeEach(module('statusControllers'));

    var $httpBackend;
    var $rootScope;
    var createController;
    var appConfig;

    var serviceCreator = function(version){
        return {
            status: 'OK',
            version: version
        };
    };

    beforeEach(inject(function($injector) {
        $httpBackend = $injector.get('$httpBackend');
        $rootScope = $injector.get('$rootScope');
        appConfig = $injector.get('appConfig');
    }));

    describe('StatusCtrl', function(){
        beforeEach(inject(function($injector) {
            var $controller = $injector.get('$controller');
            createController = function() {
                return $controller('StatusCtrl', {'$scope' : $rootScope });
            };
        }));

        it('Status requests statuses from services', function() {
            $httpBackend.expectGET(appConfig.services.UserService + 'status').respond(200, serviceCreator('0.0.6'));
            $httpBackend.expectGET(appConfig.services.PlayerService + 'status').respond(200, serviceCreator('0.0.8'));

            createController();
            $httpBackend.flush();
            expect($rootScope.services).toEqual([
                {
                    name: 'UserService',
                    status: 'OK',
                    version: '0.0.6'
                },
                {
                    name: 'PlayerService',
                    status: 'OK',
                    version: '0.0.8'
                }
            ]);
        });
    });
});