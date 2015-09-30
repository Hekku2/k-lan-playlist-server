var config = require('config');
var express = require('express');
var app = express();

var routes = require('./routes');
var statusHandler = require('./handlers/status-handler.js');
var playerHandler = require('./handlers/player-handler.js');

var handlers = {
    status: statusHandler,
    player: playerHandler
};

routes.setup(app, handlers);
var server = app.listen(config.get('PlayerService.port'), function () {
    var host = server.address().address;
    var port = server.address().port;
    console.log('Example app listening at http://%s:%s', host, port);
});
