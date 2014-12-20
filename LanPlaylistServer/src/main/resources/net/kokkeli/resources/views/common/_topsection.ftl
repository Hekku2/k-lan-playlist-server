<nav class="navbar navbar-default nav-tabs" role="navigation">
  <div class="container-fluid">
    <div class="collapse navbar-collapse in">
      <ul class="nav navbar-nav">
		<li class="<#if tab?? && tab == 0>active</#if>"><a href="/index">View</a></li>
		<#if getUserRole == 3>
			<li class="<#if tab?? && tab == 1>active</#if>"><a href="/users">Users</a></li>
			<li class="<#if tab?? && tab == 2>active</#if>"><a href="/playlists">Playlists</a></li>
			<li class="<#if tab?? && tab == 3>active</#if>"><a href="/management">Management</a></li>
		</#if>
      </ul>
      <ul class="nav navbar-nav navbar-right">
        <#if getUsername??>
			<li><a href="/users/${getUserId}">${getUsername}</a></li>
			<li><a href="/authentication/logout">Logout</a></li>
		<#else>
			<li><a href="/authentication">Log in</a></li>
		</#if>
      </ul>
    </div>
  </div>
</nav>