INSERT INTO users (UserName, Role, PasswordHash) VALUES ('admin', 2, '4c971b7a598e1c1fb09e6fe1750c6fd3ed1e73ff');
INSERT INTO users (UserName, Role, PasswordHash) VALUES ('user', 1, '4c971b7a598e1c1fb09e6fe1750c6fd3ed1e73ff');

-- Tracks
INSERT INTO tracks (Track, Artist, Location, Uploader) VALUES ('Seitan is guud', 'Lucifer Virtanen', 'X:\Music\song.ogg',(SELECT Id FROM users WHERE UserName like 'admin'));
INSERT INTO tracks (Track, Artist, Location, Uploader) VALUES ('March enemy hell matti', 'Lucifer Virtanen', 'X:\Music\song3.ogg',(SELECT Id FROM users WHERE UserName like 'admin'));
INSERT INTO tracks (Track, Artist, Location, Uploader) VALUES ('Jarmonet', 'Raimo Belsebup', 'X:\Music\song2.ogg',(SELECT Id FROM users WHERE UserName like 'user'));
INSERT INTO tracks (Track, Artist, Location, Uploader) VALUES ('Mursujyrä', 'Marssimailat', 'X:\Music\Marssimailat - Mursujyrä.ogg',(SELECT Id FROM users WHERE UserName like 'admin'));
INSERT INTO tracks (Track, Artist, Location, Uploader) VALUES ('Katujyrä', 'Marssimailat', 'X:\Music\Marssimailat - Katujyrä.ogg',(SELECT Id FROM users WHERE UserName like 'admin'));

-- Playlists
INSERT INTO playlists (Name) VALUES ('Joululan 2014');
INSERT INTO playlists (Name) VALUES ('Empty list');

-- Tracks to playlists
INSERT INTO tracks_playlists (TrackId, PlayListId)
VALUES (
    (SELECT Id FROM tracks WHERE Track like 'Seitan is guud' and Artist like 'Lucifer Virtanen'),
    (SELECT Id FROM playlists WHERE Name like 'Joululan 2014')
);
INSERT INTO tracks_playlists (TrackId, PlayListId)
VALUES (
    (SELECT Id FROM tracks WHERE Track like 'March enemy hell matti' and Artist like 'Lucifer Virtanen'),
    (SELECT Id FROM playlists WHERE Name like 'Joululan 2014')
);
INSERT INTO tracks_playlists (TrackId, PlayListId)
VALUES (
    (SELECT Id FROM tracks WHERE Track like 'Jarmonet' and Artist like 'Raimo Belsebup'),
    (SELECT Id FROM playlists WHERE Name like 'Joululan 2014')
);
INSERT INTO tracks_playlists (TrackId, PlayListId)
VALUES (
    (SELECT Id FROM tracks WHERE Track like 'Mursujyrä' and Artist like 'Marssimailat'),
    (SELECT Id FROM playlists WHERE Name like 'Joululan 2014')
);
INSERT INTO tracks_playlists (TrackId, PlayListId)
VALUES (
    (SELECT Id FROM tracks WHERE Track like 'Katujyrä' and Artist like 'Marssimailat'),
    (SELECT Id FROM playlists WHERE Name like 'Joululan 2014')
);

INSERT INTO fetch_requests(Location, Handler, DestinationFile, LastUpdated, FetchStatus, Track)
VALUES (
	'http://www.example.com/',	'mockHandler',	'Marssimailat - Mursujyrä.ogg',	datetime('now'),0,
	(SELECT Id FROM tracks WHERE Track like 'Mursujyrä' and Artist like 'Marssimailat')
);
INSERT INTO fetch_requests(Location, Handler, DestinationFile, LastUpdated, FetchStatus, Track)
VALUES (
	'http://www.example.com/',	'mockHandler',	'Marssimailat - Katujyrä.ogg',	datetime('now'),3,
	(SELECT Id FROM tracks WHERE Track like 'Katujyrä' and Artist like 'Marssimailat')
);