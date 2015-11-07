var config = require('config');
var cp = require('child_process');

exports.initializeTestData = function (){
    var parameterOrEmpty = function(parameter, configuration){
        var value = config.get(configuration);
        if (value)
            return '--' + parameter + '=' + value;
        else
            return '';
    };

    var userParameter = parameterOrEmpty('user', 'TestSettings.admin.user');
    var passwordParameter = parameterOrEmpty('password', 'TestSettings.admin.password');

    var database = config.get('Database.database');
    var initializeUsers = config.get('TestSettings.initialize-users');

    var userInitializationParameter = initializeUsers ? ' < ../db/initialize_users.sql': '';

    var command = 'mysql '+ userParameter + ' ' + passwordParameter + ' ' + database + userInitializationParameter + ' < ../db/insert_test_data.sql';
    cp.exec(command, function(error,stdout,stderr) {
        if (error){
            console.log(error);
        }
    });
};