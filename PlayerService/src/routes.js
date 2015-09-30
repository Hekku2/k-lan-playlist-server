exports.setup = function setup(app, handlers) {
    app.get('/api/status', handlers.status.status);
    app.get('/api/play', handlers.player.play);
    app.get('/api/stop', handlers.player.stop);
};