@ECHO OFF
SET startClass=net.kokkeli.player.webservice.PlayerServiceRunner
SET startMethod=main
SET stopMethod=stop
SET serviceName=Lanplayer-Player
SET description="Lanplayer Player service"
SET classPath=%CD%\lanplayer.jar
SET settingsFile=settings\publish.dat
prunsrv.exe //IS//%serviceName% --Description=%description% --StartMode=jvm --StartClass=%startClass% --StartParams=%settingsFile% --StopMode=jvm --StopClass=%startClass% --Classpath=%classPath% --StartMethod=%startMethod% --StopMethod=%stopMethod%