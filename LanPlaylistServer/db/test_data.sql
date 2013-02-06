INSERT INTO users (UserName, Role, PasswordHash) VALUES ('admin', 2, '4c971b7a598e1c1fb09e6fe1750c6fd3ed1e73ff');
INSERT INTO users (UserName, Role, PasswordHash) VALUES ('user', 1, '4c971b7a598e1c1fb09e6fe1750c6fd3ed1e73ff');

-- Tracks
INSERT INTO tracks (Track, Artist, Location) VALUES ('Seitan is guud', 'Lucifer Virtanen', 'D:\Music\song.ogg');
INSERT INTO tracks (Track, Artist, Location) VALUES ('March enemy hell matti', 'Lucifer Virtanen', 'D:\Music\song3.ogg');
INSERT INTO tracks (Track, Artist, Location) VALUES ('Jarmonet', 'Raimo Belsebup', 'D:\Music\song2.ogg');

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