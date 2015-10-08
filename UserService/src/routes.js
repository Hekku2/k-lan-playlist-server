exports.setup = function setup(app, handlers) {
    app.get('/api/status', handlers.status.status);
    app.get('/api/users', handlers.user.list);
    app.get('/api/user/:id', handlers.user.single);
    app.post('/api/user/:id', handlers.user.update);
};