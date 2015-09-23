#!/usr/bin/env bash

apt-get update
apt-get install -y git

apt-get install -y g++

#Node js
curl --silent --location https://deb.nodesource.com/setup_4.x | bash -
apt-get install --yes nodejs

apt-get install -y vlc pulseaudio

mkdir lanplayer
chown vagrant lanplayer

npm install gulp -g