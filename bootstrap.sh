#!/usr/bin/env bash

apt-get -qq update
apt-get -qq install -y git

#Node js
curl --silent --location https://deb.nodesource.com/setup_4.x | bash -
apt-get -qq install -y nodejs
apt-get -qq install -y vlc pulseaudio

#Install global npm stuff.
npm config set loglevel warn
npm install pm2 -g
npm install gulp -g

#Clone the base project (currently from branch)
git clone https://github.com/Hekku2/k-lan-playlist-server.git -b Branch_nodejs
chown -R vagrant k-lan-playlist-server

#Install 
(cd k-lan-playlist-server/PlayerService/ && npm install)

#Start VLC as vagrant.
su - vagrant -c 'vlc -d -I http --http-port 9999 --http-password test --sout '\''#standard{access=http,mux=ogg,dst=0.0.0.0:8081}'\'' --http-host=127.0.0.1 --sout-keep'

#Start services
su - vagrant -c 'cd k-lan-playlist-server/PlayerService/ && pm2 start src/player-service.js --watch'



