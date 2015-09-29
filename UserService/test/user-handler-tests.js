var assert = require('assert');
var sinon = require('sinon');
var Promise = require('sequelize').Promise;
var db = require('../src/db/user-operations.js')

describe('user-handler', function(){
    var response;
    var request;
    
    var sandbox;
    var queryStub;
    
    var service;
    
    beforeEach(function() {
        sandbox = sinon.sandbox.create();
        queryStub = sandbox.stub(db, 'users');
        service = require('../src/handlers/user-handler.js');
        
        response = {
            content: undefined,
            status: undefined,
            send: function(msg){
                this.content = msg;
            },
            sendStatus: function(status){
                this.status = status;
            }
        };
    });
    
    afterEach(function() {
        sandbox.restore();
    });
    
    describe('#list()', function(){
        it('should return list of users', function (done){
            //queryStub.returns(Promise.reject('VMP'));
            var promise = Promise.resolve([{
                id:20,
                username: 'test-user'
            }]);
            queryStub.returns(promise);
            service.list(request, response);
            promise.then(function(x){
                var user = response.content[0];
                
                assert.equal(20, user.id);
                assert.equal('test-user', user.username);
                done();
            });
        });
    });
});