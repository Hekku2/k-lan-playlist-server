var config = require('config');
var cp = require('child_process');

exports.initializeTestData = function (){
    var userParameter = '--user=' + config.get('TestSettings.admin.user');

    var passwordParameter;
    var password = config.get('TestSettings.admin.password');
    if (password)
        passwordParameter = '--password=' + config.get('TestSettings.admin.password');
    else
        passwordParameter = '';

    var database = config.get('Database.database');
    var users = 'mysql '+ userParameter + ' ' + passwordParameter + ' ' + database+ ' < ../db/initialize_users.sql';
    console.log(users);
    cp.exec(users, function(error,stdout,stderr) {
        if (error){
            console.log(error);
            return;
        }

        var testData = 'mysql '+ userParameter + ' ' + passwordParameter + ' ' + database+ ' < ../db/insert_test_data.sql';
        cp.exec(testData, function(error,stdout,stderr) {
            if (error){
                console.log(error);
            }
        });
    });
};

