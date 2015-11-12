var config = require('config');
var express = require('express');
var app = express();

var routes = require('./routes');
var indexHandler = require('./handlers/index-handler.js');

var innerServer;

exports.start = function(port){
    app.set('views', __dirname + '/views');
    app.set('view engine', 'jade');

    routes.setup(app, indexHandler);
    innerServer = app.listen(port, function () {
        console.log('Example app listening at http://localhost:%s',  port);
    });
};

exports.stop = function(){
    innerServer.close();
};