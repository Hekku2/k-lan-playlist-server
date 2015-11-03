var express = require('express');

exports.setup = function setup(app, handlers) {
    app.use('/js/angular/', express.static('bower_components/angular/'));
    app.use('/js/angular-route/', express.static('bower_components/angular-route/'));
    app.use('/js/bootstrap/', express.static('bower_components/bootstrap/dist/js/'));
    app.use('/js/jquery/', express.static('bower_components/jquery/dist/'));
    app.use('/js/', express.static('src/public/js/'));

    app.use('/css/bootstrap/', express.static('bower_components/bootstrap/dist/css/'));
    app.use('/css/fonts/', express.static('bower_components/bootstrap/dist/fonts/'));

    app.get('/users', handlers.users.users);
    app.get('/user/:userId', handlers.users.user);
};