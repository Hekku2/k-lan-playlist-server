SET startClass=net.kokkeli.player.PlayerServiceRunner
SET serviceName=Lanplayer-Player
SET description="Lanplayer Player service"
SET classPath=%CD%\lanplayer.jar
tools\prunsrv.exe //IS//%serviceName% --Description=%description% --StartMode=jvm --StartClass=%startClass% --StartParams=start --StopMode=jvm --StopClass=%startClass% --StopParams=stop --Classpath=%classPath%