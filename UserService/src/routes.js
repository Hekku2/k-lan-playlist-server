exports.setup = function setup(app, handlers) {
    app.get('/api/status', handlers.status.status);
    app.get('/api/users', handlers.user.list);
};