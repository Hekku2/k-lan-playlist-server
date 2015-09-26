USE lanplayer;

DELETE FROM logs;
DELETE FROM fetch_requests;
DELETE FROM tracks_playlists;
DELETE FROM playlists;
DELETE FROM tracks;
DELETE FROM users;

INSERT INTO users (UserName, Role, PasswordHash) VALUES ('admin', 3, '');
INSERT INTO users (UserName, Role, PasswordHash) VALUES ('user', 2, '');

-- Tracks
INSERT INTO tracks (Track, Artist, Location, Uploader) VALUES ('Seitan is guud', 'Lucifer Virtanen', 'X:\Music\song.ogg',(SELECT Id FROM users WHERE UserName like 'admin'));
INSERT INTO tracks (Track, Artist, Location, Uploader) VALUES ('March enemy hell matti', 'Lucifer Virtanen', 'X:\Music\song3.ogg',(SELECT Id FROM users WHERE UserName like 'admin'));
INSERT INTO tracks (Track, Artist, Location, Uploader) VALUES ('Jarmonet', 'Raimo Belsebup', 'X:\Music\song2.ogg',(SELECT Id FROM users WHERE UserName like 'user'));
INSERT INTO tracks (Track, Artist, Location, Uploader) VALUES ('Mursujyrä', 'Marssimailat', 'X:\Music\Marssimailat - Mursujyrä.ogg',(SELECT Id FROM users WHERE UserName like 'admin'));
INSERT INTO tracks (Track, Artist, Location, Uploader) VALUES ('Katujyrä', 'Marssimailat', 'X:\Music\Marssimailat - Katujyrä.ogg',(SELECT Id FROM users WHERE UserName like 'admin'));
INSERT INTO tracks (Track, Artist, Location, Uploader) VALUES ('Katujyrä', 'Marssimailat', 'X:\Music\Marssimailat - Katujyrä.ogg', null);

-- Playlists
INSERT INTO playlists (Name) VALUES ('Test playlist 2014');
INSERT INTO playlists (Name) VALUES ('Test playlist 2015');

-- Tracks to playlists
INSERT INTO tracks_playlists (TrackId, PlayListId)
VALUES (
    (SELECT Id FROM tracks WHERE Track like 'Seitan is guud' and Artist like 'Lucifer Virtanen'),
    (SELECT Id FROM playlists WHERE Name like 'Test playlist 2014')
);
INSERT INTO tracks_playlists (TrackId, PlayListId)
VALUES (
    (SELECT Id FROM tracks WHERE Track like 'March enemy hell matti' and Artist like 'Lucifer Virtanen'),
    (SELECT Id FROM playlists WHERE Name like 'Test playlist 2014')
);
INSERT INTO tracks_playlists (TrackId, PlayListId)
VALUES (
    (SELECT Id FROM tracks WHERE Track like 'Jarmonet' and Artist like 'Raimo Belsebup'),
    (SELECT Id FROM playlists WHERE Name like 'Test playlist 2014')
);
INSERT INTO tracks_playlists (TrackId, PlayListId)
VALUES (
    (SELECT Id FROM tracks WHERE Track like 'Mursujyrä' and Artist like 'Marssimailat'),
    (SELECT Id FROM playlists WHERE Name like 'Test playlist 2014')
);
INSERT INTO tracks_playlists (TrackId, PlayListId)
VALUES (
    (SELECT Id FROM tracks WHERE Track like 'Katujyrä' and Artist like 'Marssimailat'),
    (SELECT Id FROM playlists WHERE Name like 'Test playlist 2014')
);

INSERT INTO fetch_requests(Location, Handler, DestinationFile, LastUpdated, FetchStatus, Track)
VALUES (
	'http://www.example.com/',	'',	'Marssimailat - Mursujyrä.ogg',	datetime('now'),0,
	(SELECT Id FROM tracks WHERE Track like 'Mursujyrä' and Artist like 'Marssimailat')
);
INSERT INTO fetch_requests(Location, Handler, DestinationFile, LastUpdated, FetchStatus, Track)
VALUES (
	'http://www.example.com/',	'',	'Marssimailat - Katujyrä.ogg',	datetime('now'),3,
	(SELECT Id FROM tracks WHERE Track like 'Katujyrä' and Artist like 'Marssimailat')
);

INSERT INTO logs (Timestamp, Severity, Message, Source) VALUES ('2014-10-22 07:41:27', '2', 'User created.', '');
INSERT INTO logs (Timestamp, Severity, Message, Source) VALUES ('2014-10-22 07:41:28', '2', 'User created.', '');
INSERT INTO logs (Timestamp, Severity, Message, Source) VALUES ('2014-10-22 07:41:29', '3', 'Shitter exploded.', '');
INSERT INTO logs (Timestamp, Severity, Message, Source) VALUES ('2014-10-22 07:41:30', '2', 'User created.', '');
INSERT INTO logs (Timestamp, Severity, Message, Source) VALUES ('2014-10-22 07:41:31', '2', 'User created.', '');
INSERT INTO logs (Timestamp, Severity, Message, Source) VALUES ('2014-10-22 07:41:32', '2', 'User created.', '');
INSERT INTO logs (Timestamp, Severity, Message, Source) VALUES ('2014-10-22 07:41:33', '2', 'User created.', '');
INSERT INTO logs (Timestamp, Severity, Message, Source) VALUES ('2014-10-22 07:41:34', '3', 'Something went terribly wrong.', '');
INSERT INTO logs (Timestamp, Severity, Message, Source) VALUES ('2014-10-22 07:41:35', '2', 'User created.', '');
INSERT INTO logs (Timestamp, Severity, Message, Source) VALUES ('2014-10-22 07:41:36', '2', 'User created.', '');
INSERT INTO logs (Timestamp, Severity, Message, Source) VALUES ('2014-10-22 07:41:37', '2', 'User created.', '');
INSERT INTO logs (Timestamp, Severity, Message, Source) VALUES ('2014-10-22 07:41:38', '2', 'User created.', '');