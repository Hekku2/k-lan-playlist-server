var userOperations = require('../user-operations.js');

exports.list = function(req, res) {
    var users = userOperations.users();
    users.then(function(result){
        res.send(result);
    }).error(function(error){
        res.sendStatus(500);
    });
};