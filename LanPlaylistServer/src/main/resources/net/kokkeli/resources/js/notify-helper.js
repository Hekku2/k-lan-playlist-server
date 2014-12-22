function showSuccess(event){
	$.notify(event, { 
		position:"top-center",
		className: "success"
	});
}

function showError(event){
	$.notify(event.responseText, { 
		position:"top-center",
		className: "error"
	});
}