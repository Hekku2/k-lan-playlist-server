<div class="top-section">

	<a class="menu-item <#if tab?? && tab == 0>selected</#if>" href="http://localhost:9998/"><div>View</div></a>
	<a class="menu-item <#if tab?? && tab == 1>selected</#if>" href="http://localhost:9998/users"><div>Users</div></a>
	<a class="menu-item <#if tab?? && tab == 2>selected</#if>" href="http://localhost:9998/playlists"><div>Playlists</div></a>
	<div class="top-page-bar"></div>
	
	<div class="logout-item">${getUsername}</div>
	<div class="logout-item"><a href="http://localhost:9998/authentication/logout">Logout</a></div>
</div>
