var express = require('express');
var bodyParser = require('body-parser');
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
    app.use(bodyParser.json());
    app.use(function(req, res, next) {
        res.header("Access-Control-Allow-Origin", "*");
        res.header("Access-Control-Allow-Headers", "Origin, X-Requested-With, Content-Type, Accept");
        next();
    });

    routes.setup(app, handlers);

    innerServer = app.listen(port, function () {
        console.log('Example app listening at http://localhost:%s',  port);
    });
};

exports.stop = function(){
    innerServer.close();
};