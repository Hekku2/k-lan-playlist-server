var gulp = require('gulp'); 
var jshint = require('gulp-jshint');

// JS hint task
gulp.task('jshint', function() {
  gulp.src('./src/*.js')
    .pipe(jshint('.jshintrc'))
    .pipe(jshint.reporter('default'));
});

gulp.task('default', ['jshint'], function() {
  // watch for JS changes
  gulp.watch('./src/*.js', function() {
    gulp.run('jshint');
  });
});