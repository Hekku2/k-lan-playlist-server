<div class="upload-selection-row">
	<a class="btn <#if selectedMethod?? && selectedMethod == 0>selected-method</#if>" href="/playlists/add/upload/${getModel.getPlaylistId}">
		Upload
	</a>
	<a class="btn <#if selectedMethod?? && selectedMethod == 1>selected-method</#if>" href="/playlists/add/youtube/${getModel.getPlaylistId}">
		Youtube
	</a>
</div>