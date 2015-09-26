var config = require('config');
var Sequelize = require('sequelize');
var sequelize = new Sequelize('lanplayer', config.get('Database.user'), config.get('Database.password'), {
    host: config.get('Database.host'),
    dialect: 'mysql'
});

var User = sequelize.define('user', {
    username: {
        type: Sequelize.STRING,
    }
}, {
    timestamps: false
});

exports.users = function(){
    return User.findAll();
};

