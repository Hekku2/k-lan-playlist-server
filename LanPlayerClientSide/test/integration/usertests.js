describe("User tests", function() {
    it('Users-page has list of users', function() {
        browser.get('users');

        expect(element.all(by.css('tbody tr:nth-child(1) td:nth-child(2)')).first().getText()).toMatch('admin');
        expect(element.all(by.css('tbody tr:nth-child(2) td:nth-child(2)')).first().getText()).toMatch('user');
    });
});