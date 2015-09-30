var config = require('config');

var vlc = require('vlc-control-node');
vlc.init({
    ip: config.get('Vlc.http-ip'),
    port: config.get('Vlc.http-port'),
    user: config.get('Vlc.http-username'),
    password: config.get('Vlc.http-password')}
);

exports.play = function status(req, res) {
    res.send('mock1');
};

exports.stop = function status(req, res) {
    res.send('mock2');
};