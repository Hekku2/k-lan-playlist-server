CREATE TABLE users(
    Id INTEGER NOT NULL PRIMARY KEY,
    UserName varchar(255) UNIQUE,
    Role int 
);