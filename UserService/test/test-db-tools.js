var config = require('config');
var cp = require('child_process');

exports.initializeTestData = function (){
    var user = '--user=' + config.get('TestSettings.admin.user');
    var password = '--password=' + config.get('TestSettings.admin.password');
    var database = config.get('Database.database');
    var users = 'mysql '+ user + ' ' + password + ' ' + database+ ' < ../db/initialize_users.sql';
    console.log(users);
    cp.exec(users, function(error,stdout,stderr) {
        if (error){
            console.log(error);
            return;
        }

        var testData = 'mysql '+ user + ' ' + password + ' ' + database+ ' < ../db/insert_test_data.sql';
        cp.exec(testData, function(error,stdout,stderr) {
            if (error){
                console.log(error);
            }
        });
    });
};

