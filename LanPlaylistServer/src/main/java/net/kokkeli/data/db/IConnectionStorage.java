package net.kokkeli.data.db;

import java.sql.Connection;

public interface IConnectionStorage {
    Connection getConnection() throws DatabaseException;
}
