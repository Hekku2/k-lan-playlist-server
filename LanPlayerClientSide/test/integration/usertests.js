var dbTools = require('../test-db-tools');

describe("User tests", function() {
    beforeEach(function() {
        dbTools.initializeTestData();
    });

    it('Users-page has list of users', function() {
        browser.get('users');

        expect(element.all(by.css('tbody tr:nth-child(1) td:nth-child(2)')).first().getText()).toMatch('admin');
        expect(element.all(by.css('tbody tr:nth-child(2) td:nth-child(2)')).first().getText()).toMatch('user');
    });
});