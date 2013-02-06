CREATE TABLE users(
    Id INTEGER NOT NULL PRIMARY KEY,
    UserName varchar(255) UNIQUE,
    Role int,
    PasswordHash varchar(255)
);

CREATE TABLE tracks(
    Id INTEGER NOT NULL PRIMARY KEY,
    Track varchar(255),
    Artist varchar(255),
    Location varchar(255)
);

CREATE TABLE playlists(
    Id INTEGER NOT NULL PRIMARY KEY,
    Name varchar(255)
);

CREATE TABLE tracks_playlists(
    TrackId INTEGER NOT NULL,
    PlayListId INTEGER NOT NULL,
    FOREIGN KEY(TrackId) REFERENCES tracks(Id),
    FOREIGN KEY(PlayListId) REFERENCES playlists(Id)
);