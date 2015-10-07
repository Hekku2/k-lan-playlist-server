var config = require('config');
var cp = require("child_process");

exports.initializeTestData = function (){
    var user = '--user=' + config.get('TestSettings.admin.user');
    var password = '--password=' + config.get('TestSettings.admin.password');
    var database = config.get('Database.database');
    var cmdLine = 'mysql '+ user + ' ' + password + ' ' + database+ ' < ../db/insert_test_data.sql';
    console.log(cmdLine);
    cp.exec(cmdLine, function(error,stdout,stderr) {
        console.log(error,stdout,stderr);
    });
};

