#!/usr/bin/env bash
apt-get -qq update
apt-get -qq install -y git

#Database configuration
debconf-set-selections <<< 'mysql-server mysql-server/root_password password root'
debconf-set-selections <<< 'mysql-server mysql-server/root_password_again password root'
apt-get -qq install -y mysql-server
apt-get -qq install -y g++

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
npm install bower -g
npm install phantomjs -g
npm install karma -g

#Youtube-dl
curl --silent https://yt-dl.org/latest/youtube-dl -o /usr/local/bin/youtube-dl
chmod a+rx /usr/local/bin/youtube-dl
add-apt-repository -y ppa:mc3man/trusty-media
apt-get -qq update
apt-get -qq install -y ffmpeg