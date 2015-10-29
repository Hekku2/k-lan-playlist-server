exports.config = {
    allScriptsTimeout: 11000,

    specs: [
        '*tests.js'
    ],

    capabilities: {
        'browserName': 'phantomjs'
    },

    baseUrl: 'http://localhost:3000/',

    framework: 'jasmine',

    jasmineNodeOpts: {
        defaultTimeoutInterval: 30000
    }
};