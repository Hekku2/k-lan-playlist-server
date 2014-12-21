$(document).ready(function(){
	$("#play").click(function () {
		$.post('/player/play', {}).always(updateNowPlaying);
	});
	
	$("#pause").click(function () {
		$.post('/player/pause', {}).always(updateNowPlaying);
	});
	
	updateAndSetTimeout();
});

function updateAndSetTimeout(){
	updateNowPlaying().always(retry);
}

function updateNowPlaying(){
	return $.get('/player/nowPlaying').fail(function(data){
		$("#now-playing").text("(Fetching failed)");
	}).done(function(data){
		$("#now-playing").text(data != "" ? data : "Nothing playing");
	});
}

function retry(){
	setTimeout(function (){
		updateAndSetTimeout();
	}, 5000);
}