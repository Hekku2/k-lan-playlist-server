/**
 * Functions related to VLC upload.
 */

$(document).ready(function(){
	$('form').submit(function(event){
		event.preventDefault();
		$('input[type="submit"]').button('loading')
		$.post("", $( "form" ).serialize()).fail(uploadfailed).done(uploadSuccess).always(resetButton);
	});
});

function uploadfailed(event){
	$.notify(event.responseText, { 
		position:"top-center",
		className: "error"
	});
}

function uploadSuccess(event){
	$.notify(event, { 
		position:"top-center",
		className: "success"
	});
	clearForm();
}

function resetButton(){
	$('input[type="submit"]').button('reset')
}

function clearForm(){
	$('form').find("input[type=text], textarea").val("");
}