package net.kokkeli.data.db;

import java.util.Collection;

import com.sun.jersey.api.NotFoundException;

/**
 * DataBase interface for common database functions
 * @author Hekku2
 *
 */
public interface IDatabase<T> {

    void CheckDatabaseFormat() throws DatabaseException;
    
    T get(long id) throws DatabaseException, NotFoundException;
    
    Collection<T> get() throws DatabaseException;
    
    void add(T item) throws DatabaseException;
}
