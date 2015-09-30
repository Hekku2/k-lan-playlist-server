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

routes.setup(app, handlers);
var server = app.listen(config.get('UserService.port'), function () {
    var host = server.address().address;
    var port = server.address().port;
    console.log('Example app listening at http://%s:%s', host, port);
});
