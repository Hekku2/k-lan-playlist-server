<h3>Select method:</h3>
<ul class="nav nav-tabs" role="tablist">
  <li <#if selectedMethod?? && selectedMethod == 0>class="active"</#if>><a id="upload" href="/playlists/add/upload/${getModel.getPlaylistId}">Upload</a></li>
  <li <#if selectedMethod?? && selectedMethod == 2>class="active"</#if>><a id="youtube-dl" href="/playlists/add/youtubeDl/${getModel.getPlaylistId}">Youtube DL</a></li>
  <li <#if selectedMethod?? && selectedMethod == 1>class="active"</#if>><a id="vlc" href="/playlists/add/vlc/${getModel.getPlaylistId}">Vlc</a></li>
</ul>