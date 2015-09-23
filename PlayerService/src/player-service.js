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

var server = app.listen(8080, function () {
    var host = server.address().address;
    var port = server.address().port;
    console.log('Example app listening at http://%s:%s', host, port);
});
