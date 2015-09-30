#!/usr/bin/env bash
apt-get -qq update
apt-get -qq install -y git

#Database configuration
debconf-set-selections <<< 'mysql-server mysql-server/root_password password root'
debconf-set-selections <<< 'mysql-server mysql-server/root_password_again password root'
apt-get -qq install -y mysql-server


#Node js
curl --silent --location https://deb.nodesource.com/setup_4.x | bash -
apt-get -qq install -y nodejs
apt-get -qq install -y vlc pulseaudio

#Install global npm stuff.
npm config set loglevel warn
npm install pm2 -g
npm install gulp -g
npm install mocha -g
npm install jenkins-mocha -g 

#Youtube-dl
curl --silent https://yt-dl.org/latest/youtube-dl -o /usr/local/bin/youtube-dl
chmod a+rx /usr/local/bin/youtube-dl
add-apt-repository -y ppa:mc3man/trusty-media
apt-get -qq update
apt-get -qq install -y ffmpeg


#Clone the base project (currently from branch)
git clone https://github.com/Hekku2/k-lan-playlist-server.git -b Branch_nodejs
chown -R vagrant k-lan-playlist-server

#Install 
(cd k-lan-playlist-server/PlayerService/ && npm install)
(cd k-lan-playlist-server/UserService/ && npm install)

#Start VLC as vagrant.
su - vagrant -c 'vlc -d -I http --http-port 9999 --http-password test --sout '\''#standard{access=http,mux=ogg,dst=0.0.0.0:9090}'\'' --http-host=127.0.0.1 --sout-keep'

#Start services
su - vagrant -c 'cd k-lan-playlist-server/PlayerService/ && pm2 -s start src/player-service.js --watch'
su - vagrant -c 'cd k-lan-playlist-server/UserService/ && pm2 -s start src/user-service.js --watch'

#Initialiaze database for dev deployment
mysql --user=root --password=root < k-lan-playlist-server/db/database_initialization.sql
mysql --user=root --password=root < k-lan-playlist-server/db/insert_test_data.sql

