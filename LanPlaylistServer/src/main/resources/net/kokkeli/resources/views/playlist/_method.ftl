<h3>Select method:</h3>
<ul class="nav nav-tabs" role="tablist">
  <li <#if selectedMethod?? && selectedMethod == 0>class="active"</#if>><a href="/playlists/add/upload/${getModel.getPlaylistId}">Upload</a></li>
  <li <#if selectedMethod?? && selectedMethod == 1>class="active"</#if>><a href="/playlists/add/vlc/${getModel.getPlaylistId}">Vlc</a></li>
</ul>