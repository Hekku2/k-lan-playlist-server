var packageJson = require('../../package.json');

exports.status = function status(req, res) {
    res.send({
        status: 'OK',
        version: packageJson.version
    });
};