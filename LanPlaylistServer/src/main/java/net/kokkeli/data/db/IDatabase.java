package net.kokkeli.data.db;

import java.util.Collection;

/**
 * DataBase interface for common database functions
 * @author Hekku2
 *
 */
public interface IDatabase<T> {

    void CheckDatabaseFormat() throws DatabaseException;
    
    T get(long id) throws DatabaseException;
    
    Collection<T> get() throws DatabaseException;
}
