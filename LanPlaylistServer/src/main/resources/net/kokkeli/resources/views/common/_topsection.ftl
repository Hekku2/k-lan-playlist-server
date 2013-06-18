<div class="top-section">

	<a class="menu-item <#if tab?? && tab == 0>selected</#if>" href="/"><div>View</div></a>
	<a class="menu-item <#if tab?? && tab == 1>selected</#if>" href="/users"><div>Users</div></a>
	<a class="menu-item <#if tab?? && tab == 2>selected</#if>" href="/playlists"><div>Playlists</div></a>
	<a class="menu-item <#if tab?? && tab == 3>selected</#if>" href="/management"><div>Management</div></a>
	<div class="top-page-bar"></div>
	
	<div class="logout-item">${getUsername}</div>
	<div class="logout-item"><a href="/authentication/logout">Logout</a></div>
</div>
