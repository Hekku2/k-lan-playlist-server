//About 20mb
var maxFileSize = 10399744 * 2;

$(document).ready(function(){
	$('#validation-error').hide();
	
	$('#file').bind('change', function() {
		if (this.files.length > 0 && this.files[0].size > maxFileSize){
			event.preventDefault();
			$('#validation-error').show();
			$('#validation-error').text('File size is too big.');
		}else {
			$('#validation-error').text('');
			$('#validation-error').hide();
		}
	});
	
	$('#form').submit(function(event){
		var files = document.getElementById('file').files;
		
		if (files.length < 1){
			event.preventDefault();
			$('#validation-error').show();
			$('#validation-error').text('File is needed.');
			return;
		}
		
		//TODO Check type
		//TODO check file size
		if (files[0].size > maxFileSize){
			event.preventDefault();
			$('#validation-error').show();
			$('#validation-error').text('File size too big.');
			return;
		}
		
		$('#validation-error').text('');
		$('#validation-error').hide();
	});
});
