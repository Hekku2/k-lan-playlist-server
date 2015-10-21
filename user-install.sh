#Clone the base project (currently from branch)
git clone https://github.com/Hekku2/k-lan-playlist-server.git -b Branch_nodejs

#Install 
(cd k-lan-playlist-server/PlayerService/ && npm install)
(cd k-lan-playlist-server/UserService/ && npm install)
(cd k-lan-playlist-server/LanPlayerClientSide/ && npm install && bower install --config.interactive=false)

#Start VLC as damen
vlc -d -I http --http-port 9999 --http-password test --sout '#standard{access=http,mux=ogg,dst=0.0.0.0:9090}' --http-host=127.0.0.1 --sout-keep

#Start services
(cd k-lan-playlist-server/PlayerService/ && pm2 -s start src/start.js --watch --name player-service)
(cd k-lan-playlist-server/UserService/ && pm2 -s start src/start.js --watch --name user-service)
(cd k-lan-playlist-server/LanPlayerClientSide/ && pm2 -s start src/start.js --watch --name client-side)
pm2 -s save

#Initialiaze database for dev deployment
mysql --user=root --password=root < k-lan-playlist-server/db/database_initialization.sql
mysql --user=root --password=root < k-lan-playlist-server/db/initialize_users.sql
mysql --user=root --password=root < k-lan-playlist-server/db/insert_test_data.sql