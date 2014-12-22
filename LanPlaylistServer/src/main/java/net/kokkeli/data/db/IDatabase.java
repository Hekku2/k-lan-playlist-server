package net.kokkeli.data.db;

import java.util.Collection;

/**
 * DataBase interface for common database functions
 * @author Hekku2
 *
 */
public interface IDatabase<T> {

    /**
     * Checks database format. Throws DatabaseException if format is not fine.
     * @throws DatabaseException
     */
    void CheckDatabaseFormat() throws DatabaseException;
    
    T get(long id) throws DatabaseException, NotFoundInDatabaseException;
    
    /**
     * Returns all items from database.
     * @return Collection of items
     * @throws DatabaseException Thrown if there is problem with database.
     */
    Collection<T> get() throws DatabaseException;
    
    /**
     * Adds item to database
     * @param item Item to add
     * @return Added item, with id, if it has one.
     * @throws DatabaseException Thrown if there is a problem with the database.
     */
    T add(T item) throws DatabaseException;
}
