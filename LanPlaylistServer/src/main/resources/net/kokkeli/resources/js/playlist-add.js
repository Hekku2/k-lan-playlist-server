//About 20mb
var maxFileSize = 10399744 * 2;
var file;
var submitButtonSelector = 'input[type="submit"]';

$(document).ready(function(){
    $('form').fileupload({
        url: "",
        autoUpload: false,
        add: function (e, data) {
        	$('#filename').text(data.originalFiles[0].name);
            data.context = $(submitButtonSelector).click(function () {
            	data.submit().fail(uploadfailed).done(uploadSuccess).always(resetButton);
            });
        }
    });
    
	$('form').submit(function(event){
		event.preventDefault();
		$(submitButtonSelector).button('loading')
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
	$(submitButtonSelector).button('reset')
}

function clearForm(){
	$('form').find("input[type=text], textarea").val("");
}