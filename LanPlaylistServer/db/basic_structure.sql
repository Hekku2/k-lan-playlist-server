CREATE TABLE users(
    Id INTEGER NOT NULL PRIMARY KEY,
    UserName TEXT UNIQUE,
    Role int,
    PasswordHash TEXT
);

CREATE TABLE tracks(
    Id INTEGER NOT NULL PRIMARY KEY,
    Track TEXT,
    Artist TEXT,
    Location TEXT,
    Uploader INTEGER NOT NULL,
    FOREIGN KEY(Uploader) REFERENCES users(Id)
);

CREATE TABLE playlists(
    Id INTEGER NOT NULL PRIMARY KEY,
    Name TEXT
);

CREATE TABLE tracks_playlists(
    TrackId INTEGER NOT NULL,
    PlayListId INTEGER NOT NULL,
    FOREIGN KEY(TrackId) REFERENCES tracks(Id),
    FOREIGN KEY(PlayListId) REFERENCES playlists(Id)
);

CREATE TABLE fetch_requests(
	Id INTEGER NOT NULL PRIMARY KEY,
	Location TEXT NOT NULL,
	Handler TEXT NOT NULL,
	DestinationFile TEXT NOT NULL,
	LastUpdated TEXT NOT NULL,
	FetchStatus INTEGER NOT NULL,
	Track INTEGER NOT NULL,
	FOREIGN KEY(Track) REFERENCES tracks(Id)
);