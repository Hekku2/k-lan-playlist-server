package net.kokkeli.data.services;

import java.util.Collection;
import java.util.Date;

import com.google.inject.Inject;

import net.kokkeli.data.FetchRequest;
import net.kokkeli.data.ILogger;
import net.kokkeli.data.LogSeverity;
import net.kokkeli.data.PlayList;
import net.kokkeli.data.Track;
import net.kokkeli.data.db.DatabaseException;
import net.kokkeli.data.db.IFetchRequestDatabase;
import net.kokkeli.data.db.IPlaylistDatabase;
import net.kokkeli.data.db.ITrackDatabase;
import net.kokkeli.data.db.NotFoundInDatabase;

/**
 * Service for fetch requets
 * @author Hekku2
 *
 */
public class FetchRequestService implements IFetchRequestService {
    private final ILogger logger;
    private final IFetchRequestDatabase fetchRequestDatabase;
    private final ITrackDatabase tracks;
    private final IPlaylistDatabase playlists;
    
    /**
     * Creates fetch request service
     * @param logger Logger
     * @param fetchRequestDatabase Database containing fetch requests
     * @param tracks Tracks database
     * @param playlists Playlists database
     */
    @Inject
    public FetchRequestService(ILogger logger, IFetchRequestDatabase fetchRequestDatabase, ITrackDatabase tracks, IPlaylistDatabase playlists){
        this.logger = logger;
        this.fetchRequestDatabase = fetchRequestDatabase;
        this.tracks = tracks;
        this.playlists = playlists;
    }
    
    @Override
    public Collection<FetchRequest> get() throws ServiceException {
        try {
            return fetchRequestDatabase.get();
        } catch (DatabaseException e) {
            logger.log("Something went wrong with the database while fetching fetch requests.", LogSeverity.ERROR);
            throw new ServiceException("Something when wrong with the database while fetching fetch requests.", e);
        }
    }

    @Override
    public void add(FetchRequest request) throws ServiceException {
        try {
            Track track = request.getTrack();
            if (track == null){
                logger.log("Trying to create fetch request without track.", LogSeverity.ERROR);
                throw new IllegalArgumentException("Track was not set.");
            }
            
            //If track doesnt exist, add it to database.
            if (!tracks.exists(track)){
               track = tracks.add(track);
            }
            
            //If playlist is set, add this track to it.
            if (request.getPlaylist() != null){
                PlayList playlist = playlists.get(request.getPlaylist().getId());;
                playlist.getItems().add(track);
                
                playlists.update(playlist);
            }

            request.setLastUpdated(new Date());
            fetchRequestDatabase.add(request);
        } catch (DatabaseException e) {
            logger.log("Something went wrong with the database while adding fetch request.", LogSeverity.ERROR);
            throw new ServiceException("Something went wrong with the database while adding fetch request.", e);
        } catch (NotFoundInDatabase e) {
            logger.log("Trying to create fetch request with a playlist that doesn't exist.", LogSeverity.ERROR);
            throw new IllegalArgumentException("Playlist did not exist");
        }
    }

    @Override
    public void removeHandled() throws ServiceException {
        try {
            fetchRequestDatabase.removeHandled();
        } catch (DatabaseException e) {
            logger.log("Something went wrong with the service while removing handled fetch requests.", LogSeverity.ERROR);
            throw new ServiceException("Something went wrong with the service while removing handled fetch requests.", e);
        }
    }

}
