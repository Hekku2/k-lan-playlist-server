var config = require('config');

var vlc = require('vlc-control-node');
vlc.init({
            ip: config.get('Vlc.http-ip'),
            port: config.get('Vlc.http-port'),
            user: config.get('Vlc.http-username'),
            password: config.get('Vlc.http-password')});

var express = require('express');
var app = express();

//Test
app.get('/status', function (req, res) {
    res.send('Hello World!');
});

//Player start
app.post('/start', function (req, res) {   
    res.send('Player started');
});

//Player stopped
app.post('/stop', function (req, res) {
    res.send('Player stopped');
});

var server = app.listen(config.get('PlayerService.port'), function () {
    var host = server.address().address;
    var port = server.address().port;
    console.log('Example app listening at http://%s:%s', host, port);
});
