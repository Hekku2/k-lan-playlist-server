cd LanPlaylistServer
screen -A -m -d -S server mvn exec:java -Dexec.mainClass=net.kokkeli.Program -Dexec.args=settings/testserver.dat