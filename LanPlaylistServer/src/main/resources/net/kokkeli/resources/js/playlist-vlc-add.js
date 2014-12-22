/**
 * Functions related to VLC upload.
 */

$(document).ready(function(){
	$('form').submit(function(event){
		event.preventDefault();
		$('input[type="submit"]').button('loading')
		$.post("", $( "form" ).serialize()).fail(showError).done(uploadSuccess).always(resetButton);
	});
});

function uploadSuccess(event){
	showSuccess(event);
	clearForm();
}

function resetButton(){
	$('input[type="submit"]').button('reset')
}

function clearForm(){
	$('form').find("input[type=text], textarea").val("");
}