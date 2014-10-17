<nav class="navbar navbar-default nav-tabs" role="navigation">
  <div class="container-fluid">
    <div class="collapse navbar-collapse in" id="bs-example-navbar-collapse-1">
      <ul class="nav navbar-nav">
		<li class="<#if tab?? && tab == 0>active</#if>"><a href="/">View</a></li>
		<li class="<#if tab?? && tab == 1>active</#if>"><a href="/users">Users</a></li>
		<li class="<#if tab?? && tab == 2>active</#if>"><a href="/playlists">Playlists</a></li>
		<li class="<#if tab?? && tab == 3>active</#if>"><a href="/management">Management</a></li>
      </ul>
      <ul class="nav navbar-nav navbar-right">
		<li><p class="navbar-text navbar-right">${getUsername}</p></li>
		<li><a href="/authentication/logout">Logout</a></li>
      </ul>
    </div>
  </div>
</nav>