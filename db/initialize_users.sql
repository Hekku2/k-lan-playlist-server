-- This script creates database user. In production environment, different password should be used.
GRANT USAGE ON `database`.* to 'user-service'@'localhost' identified by 'test';
GRANT USAGE ON *.* TO 'user-service'@'localhost';
GRANT SELECT, UPDATE, INSERT  ON TABLE `lanplayer`.`users` TO 'user-service'@'localhost';