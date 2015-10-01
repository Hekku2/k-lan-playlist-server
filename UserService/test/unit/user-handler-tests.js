var assert = require('assert');
var sinon = require('sinon');
var Promise = require('sequelize').Promise;
var db = require('../../src/db/user-operations.js');

describe('user-handler', function(){
    var request;
    
    var sandbox;
    var queryStub;
    
    var service = require('../../src/handlers/user-handler.js');;
    
    beforeEach('Initialize sandbox', function(done) {
        sandbox = sinon.sandbox.create();
        done();
    });
    
    afterEach('Restore sandbox', function(done) {
        sandbox.restore();
        done();
    });
        
    describe('#list()', function(){
        beforeEach('Initialize DB functions', function(done) {
            queryStub = sandbox.stub(db, 'users');

            done();
        });

        it('should return list of users', function (done){
            var promise = Promise.resolve([{
                id:20,
                username: 'test-user'
            }]);
            queryStub.returns(promise);

            var response = {
                send: function(content){
                    var user = content[0];
                    
                    assert.equal(20, user.id);
                    assert.equal('test-user', user.username);
                    done();
                }
            };
            service.list(request, response);
        });
        
        it('error should return 500 status code', function (done){
            var promise = Promise.resolve().then(function() {
                throw new Error('errors');
            });
            queryStub.returns(promise);
            var response = {
                sendStatus: function(status){
                    assert.equal(500, status);
                    done();
                }
            };
            service.list(request, response);
        });
    });

    describe('#single()', function(){
        beforeEach('Initialize DB functions', function(done) {
            queryStub = sandbox.stub(db, 'user');
            service = require('../../src/handlers/user-handler.js');
            done();
        });

        it('should return one user', function (done){
            var expectedUser = {
                id: 20,
                username: 'test-user'
            };
            var promise = Promise.resolve(expectedUser);
            queryStub.returns(promise);
            var response = {
                send: function(content){
                    var user = content;

                    assert.equal(expectedUser.id, user.id);
                    assert.equal(expectedUser.username, user.username);
                    done();
                }
            };
            request = {
                params: {
                    id: expectedUser.id
                }
            };
            service.single(request, response);
        });
    });
});