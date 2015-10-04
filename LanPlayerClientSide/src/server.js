var config = require('config');
var express = require('express');
var app = express();

var routes = require('./routes');
var userHandler = require('./handlers/user-handler.js');

var handlers = {
    users: userHandler
};

var innerServer;

exports.start = function(port){
    routes.setup(app, handlers);

    app.set('views', __dirname + '/views');
    app.set('view engine', 'jade');

    innerServer = app.listen(port, function () {
        console.log('Example app listening at http://localhost:%s',  port);
    });
};

exports.stop = function(){
    innerServer.close();
};