var gulp = require('gulp');
var jshint = require('gulp-jshint');
var mocha = require('gulp-mocha');

// JS hint task
gulp.task('jshint', function () {
    gulp.src('./src/*.js')
        .pipe(jshint('.jshintrc'))
        .pipe(jshint.reporter('default'));
});

//Mocha test run
gulp.task('mocha', function () {
    gulp.src('./test/*.js')
        .pipe(mocha())
        .on('error', function (e) {
            console.log(e);
            //Silence
        });
});

gulp.task('default', ['jshint', 'mocha'], function () {
    gulp.watch(['./test/*.js', './src/**/*.js'], function () {
        gulp.run('jshint');
        gulp.run('mocha');
    });
});