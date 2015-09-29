var assert = require('assert');
var service = require('../src/handlers/status-handler.js');

describe('status-handler', function(){
    var response;
    var request;
    
    beforeEach(function() {
        response = {
            content: undefined,
            send: function(msg){
                this.content = msg;
            }
        };
    });
    
    describe('#status()', function(){
        it('should return version and ok when everything is working', function (){
            service.status(request, response);
            assert.equal('0.0.1', response.content.version);
            assert.equal('OK', response.content.status);
        });
    });
});