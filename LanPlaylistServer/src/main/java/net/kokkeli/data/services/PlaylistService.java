package net.kokkeli.data.services;

import java.util.Collection;

import com.google.inject.Inject;

import net.kokkeli.data.ILogger;
import net.kokkeli.data.LogSeverity;
import net.kokkeli.data.PlayList;
import net.kokkeli.data.db.DatabaseException;
import net.kokkeli.data.db.IPlaylistDatabase;
import net.kokkeli.data.db.NotFoundInDatabaseException;

/**
 * PlaylistService
 * @author Hekku2
 *
 */
public class PlaylistService implements IPlaylistService {
    private final ILogger logger;
    private final IPlaylistDatabase database;
    
    /**
     * Creates new playlist service
     * @param logger Logger
     * @param playlists Playlist database
     */
    @Inject
    public PlaylistService(ILogger logger, IPlaylistDatabase playlists){
        this.logger = logger;
        this.database = playlists;
    }
    
    /**
     * Return playlist with current id.
     * @throws ServiceException Thrown when there is general with database
     * @throws NotFoundInDatabaseException Thrown when item does not exist in database.
     */
    @Override
    public PlayList getPlaylist(long currentPlaylist) throws ServiceException, NotFoundInDatabaseException {
        try {
            PlayList playlist = database.get(currentPlaylist);
            
            return playlist;
        } catch (NotFoundInDatabaseException e) {
            logger.log("No playlist found with given id.", LogSeverity.DEBUG);
            throw e;
        } catch (DatabaseException e) {
            logger.log("There was a database problem.", LogSeverity.ERROR);
            throw new ServiceException("There was something wrong with the database.",  e);
        }
    }

    @Override
    public Collection<PlayList> getIdNames() throws ServiceException {
        try {
            return database.getOnlyIdAndName();
        } catch (DatabaseException e) {
            logger.log("There was a database problem.", LogSeverity.ERROR);
            throw new ServiceException("There was something wrong with the database.",  e);
        }
    }

    @Override
    public void update(PlayList playlist) throws NotFoundInDatabaseException, ServiceException {
        try {
            database.update(playlist);
        } catch (DatabaseException e) {
            logger.log("There was a database problem.", LogSeverity.ERROR);
            throw new ServiceException("There was something wrong with the database.",  e);
        }
    }

	@Override
	public PlayList add(PlayList playlist) throws ServiceException {
        try {
            return database.add(playlist);
        } catch (DatabaseException e) {
            logger.log("Something went wrong in database: " + e.getMessage(), LogSeverity.ERROR);
            throw new ServiceException("There was problem with database.", e);
        }
	}

	@Override
	public boolean nameExists(String name) throws ServiceException {
		if (name == null || name.trim().length() == 0)
			return false;
		
		try {
			Collection<PlayList> playlists = database.getOnlyIdAndName();
			for (PlayList playList : playlists) {
				if (playList.getName().trim().equals(name.trim()))
					return true;
			}
			return false;
		} catch (DatabaseException e) {
            logger.log("Something went wrong in database: " + e.getMessage(), LogSeverity.ERROR);
            throw new ServiceException("There was problem with database.", e);
		}
	}
}
