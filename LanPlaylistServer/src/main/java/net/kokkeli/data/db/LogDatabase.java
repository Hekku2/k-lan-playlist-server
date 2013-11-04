package net.kokkeli.data.db;

import java.util.Collection;

import com.google.inject.Inject;

import net.kokkeli.ISettings;
import net.kokkeli.data.LogRow;

public class LogDatabase extends Database implements ILogDatabase {
    private LogTable logTable;
    
    /**
     * Creates Log database
     * @param settings Settigs
     */
    @Inject
    public LogDatabase(ISettings settings) {
        super(settings);
        
        logTable = new LogTable(settings.getDatabaseLocation());
    }

    @Override
    public void CheckDatabaseFormat() throws DatabaseException {
        get();
    }

    @Override
    public LogRow get(long id) throws DatabaseException, NotFoundInDatabase {
        return null;
    }

    @Override
    public Collection<LogRow> get() throws DatabaseException {
        return logTable.get();
    }

    @Override
    public LogRow add(LogRow item) throws DatabaseException {
        return logTable.insert(item);
    }

}
