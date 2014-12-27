=====================================
Kokkeli lan playlist server lanplayer
=====================================

Setup:
1. Change vlc-folder from settings/publish.dat to correct one
2. Change youtubeDL-folder from settings/publish.dat to correct one
3. Default uri (http://localhost:9998/) can be changed by changing ServerUri and Port-settings from publish.dat
4. Default login is
    username: admin
    password:kokkeli

Usage:
server.bat starts server
ripper.bat starts ripper



Troubleshooting:
* Unable to load library 'libvlc'
  1. Check that settings/publish.dat contains correct vlc-folder. There shoule be something like "VlcLocation=F:\\vlc-2.1.2"
  2. Make sure that VLC is newer than 2.1.0
  3. Make sure that VLC is for correct architechture (32bit or 64bit). 
     This is testted with 64bit version Windows 7.
