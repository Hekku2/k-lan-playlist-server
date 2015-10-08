var assert = require('assert');
var sinon = require('sinon');
var Promise = require('sequelize').Promise;
var db = require('../../src/db/user-operations.js');

describe('user-handler', function(){
    var request;
    
    var sandbox;
    var queryStub;
    
    var service = require('../../src/handlers/user-handler.js');
    
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
                json: function(content){
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
                json: function(content){
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

        it('should return 404 on nonfound', function (done){
            var promise = Promise.resolve(null);
            queryStub.returns(promise);
            var response = {
                sendStatus: function(status){
                    assert.equal(404, status);
                    done();
                }
            };
            request = {
                params: {
                    id: 123
                }
            };
            service.single(request, response);
        });

        it('should return 500 on error', function (done){
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
            service.single(request, response);
        });
    });

    describe('#update()', function(){
        var callback;
        beforeEach('Initialize DB functions', function(done) {
            dbUpdate = sandbox.stub(db, 'update');

            done();
        });

        it('should returns 200 on success', function (done){
            var promise = Promise.resolve([1]);
            dbUpdate.returns(promise);

            var response = {
                sendStatus: function(status){
                    assert.equal(200, status);
                    done();
                }
            };

            request = {
                params: {
                    id: 123
                },
                body: {
                    "username": "test"
                }
            };

            service.update(request, response);
        });

        it('should calls database with correct values', function (done){
            var promise = Promise.resolve([1]);
            dbUpdate.returns(promise);

            var response = {
                sendStatus: function(status){
                    assert(dbUpdate.calledWith({
                        id: 123,
                        username: 'test'
                    }));
                    done();
                }
            };

            request = {
                params: {
                    id: 123
                },
                body: {
                    "username": "test"
                }
            };

            service.update(request, response);
        });
    });
});