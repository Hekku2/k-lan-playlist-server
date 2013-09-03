$(document).ready(function(){
	$("button[class*='select-playlist']").click(function (event) {
		$.post('/selectPlaylist', { "id": $(event.currentTarget).attr('id') });
		
	});
});