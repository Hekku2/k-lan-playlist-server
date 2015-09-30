var assert = require('assert');
var sinon = require('sinon');
var Promise = require('sequelize').Promise;
var db = require('../../src/db/user-operations.js')

describe('user-handler', function(){
    var request;
    
    var sandbox;
    var queryStub;
    
    var service;
    
    beforeEach('Initialize sandbox', function() {
        sandbox = sinon.sandbox.create();
        queryStub = sandbox.stub(db, 'users');
        service = require('../../src/handlers/user-handler.js');
    });
    
    afterEach('Restore sandbox', function() {
        sandbox.restore();
    });
        
    describe('#list()', function(){
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
                send: function(x){},
                sendStatus: function(status){
                    assert.equal(500, status);
                    done();
                }
            };
            
            service.list(request, response);
        });
        
    });
});