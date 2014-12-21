$(document).ready(function(){
	$("button[class*='select-playlist']").click(function (event) {
		$.post('/player/selectPlaylist', { "id": $(event.currentTarget).attr('id') });
	});
});