-- This script creates database user. In production environment, different password should be used.
GRANT ALL ON `database`.* TO 'user-service'@'localhost' IDENTIFIED BY 'test';
GRANT USAGE ON *.* TO 'user-service'@'localhost';
GRANT SELECT, UPDATE, INSERT  ON TABLE `lanplayer`.`users` TO 'user-service'@'localhost';