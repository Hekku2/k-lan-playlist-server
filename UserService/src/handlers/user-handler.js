var userOperations = require('../db/user-operations.js');

exports.list = function(req, res) {
    var query = userOperations.users();
    
    var success = function(result) {
        res.json(result);
    };
    var error = function(error){
        res.sendStatus(500);
    };
    
    query.then(success).caught(error);
};

exports.single = function(req, res){
    var query = userOperations.user(req.params.id);

    var success = function(result) {
        if(!result){
            res.sendStatus(404);
            return;
        }
        res.json(result);
    };

    var error = function(error){
        res.sendStatus(500);
    };

    query.then(success).caught(error);
};

exports.update = function(req, res){
    var newData = req.body;
    //TODO Validate user
    var updated = {
        id: req.params.id,
        username: req.body.username
    };

    userOperations.update(updated);
    res.sendStatus(200);
};