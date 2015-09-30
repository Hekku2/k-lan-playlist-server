var config = require('config');
var express = require('express');
var app = express();

var routes = require('./routes');
var statusHandler = require('./handlers/status-handler.js');
var userHandler = require('./handlers/user-handler.js');

var handlers = {
    status: statusHandler,
    user: userHandler
};

var innerServer;

exports.start = function(port){
    routes.setup(app, handlers);
    
    innerServer = app.listen(port, function () {
        console.log('Example app listening at http://localhost:%s',  port);
    });
};

exports.stop = function(){
    innerServer.close();
};