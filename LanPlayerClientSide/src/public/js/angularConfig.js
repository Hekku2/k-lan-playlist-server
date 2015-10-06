angular.module('clientSideLanPlayer.config', []).constant('appConfig', {
    'services': {
        'PlayerService': 'http://localhost:8080/api/',
        'UserService': 'http://localhost:8081/api/'
    }
});