var userOperations = require('../db/user-operations.js');

exports.list = function(req, res) {
    var query = userOperations.users();
    
    var success = function(result) {
        res.send(result);
    };
    var error = function(error){
        res.sendStatus(500);
    };
    
    query.then(success).caught(error);
};

exports.single = function(req, res){
    var query = userOperations.user(req.params.id);

    var success = function(result) {
        res.send(result);
    };
    var error = function(error){
        res.sendStatus(500);
    };

    query.then(success).caught(error);
};