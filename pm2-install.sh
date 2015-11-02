#!/usr/bin/env bash
(cd PlayerService/ && pm2 -s start src/start.js --watch --name player-service)
(cd UserService/ && pm2 -s start src/start.js --watch --name user-service)
(cd LanPlayerClientSide/ && pm2 -s start src/start.js --watch --name client-side)