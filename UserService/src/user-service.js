var config = require('config');
var express = require('express');
var app = express();
var userOperations = require('./user-operations.js');

app.get('/users', function (req, res) {
    var users = userOperations.users();
    users.then(function(result){
        res.send(result);
    }).error(function(error){
        res.sendStatus(500);
    });
});

app.get('/status', function (req, res) {
    res.send('Hello World!');
});

var server = app.listen(config.get('UserService.port'), function () {
    var host = server.address().address;
    var port = server.address().port;
    console.log('Example app listening at http://%s:%s', host, port);
});
