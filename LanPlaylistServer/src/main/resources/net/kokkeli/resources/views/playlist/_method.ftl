<div class="navbar">
  <div class="navbar-inner">
    <a class="brand" href="#">Selected method:</a>
    <ul class="nav">
      <li <#if selectedMethod?? && selectedMethod == 0>class="active"</#if>><a href="/playlists/add/upload/${getModel.getPlaylistId}">Upload</a></li>
      <li <#if selectedMethod?? && selectedMethod == 1>class="active"</#if>><a href="/playlists/add/vlc/${getModel.getPlaylistId}">Vlc</a></li>
    </ul>
  </div>
</div>