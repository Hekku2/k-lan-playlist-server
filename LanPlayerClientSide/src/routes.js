var express = require('express');

exports.setup = function setup(app, handlers) {
    app.use('/js/angular/', express.static('bower_components/angular/'));
    app.use('/js/angular-route/', express.static('bower_components/angular-route/'));
    app.use('/js/', express.static('src/public/js/'));

    app.get('/users', handlers.users.users);
};