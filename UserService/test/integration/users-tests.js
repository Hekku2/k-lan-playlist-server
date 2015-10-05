var assert = require('assert');
var request = require('supertest')
var server = require('../../src/server.js');
var config = require('config');

describe('Integration tests: users', function(){
    var url = 'http://localhost:' + config.get('TestSettings.port');
    
    var adminUser = {
        id: 1,
        username: 'admin'
    };
    
    before('Initialize server', function() {
        server.start(config.get('TestSettings.port'));
    });
    
    after('Restore sandbox', function() {
        server.stop();
    });
    
    describe('#users()', function(){
        it('should return list of users', function (done){
            request(url)
            .get('/api/users')
            .expect('Content-Type', /json/)
            .expect(200)
            .end(function(err, res){
              if (err) {
                  throw err;
              }
              
              var user = res.body[0];
              assert.equal(adminUser.id, user.id);
              assert.equal(adminUser.username, user.username);
              
              done();
            });
        });
    });

    describe('#user()', function(){
        it('should return single matching user', function (done){
            request(url)
                .get('/api/user/1')
                .expect('Content-Type', /json/)
                .expect(200)
                .end(function(err, res){
                    if (err) {
                        throw err;
                    }

                    var user = res.body;
                    assert.equal(adminUser.id, user.id);
                    assert.equal(adminUser.username, user.username);

                    done();
                });
        });
        it('should return 404 if user does not exists', function (done){
            request(url)
                .get('/api/user/666')
                .expect(404)
                .end(function(err, res){
                    if (err) {
                        throw err;
                    }

                    done();
                });
        });
    });
});