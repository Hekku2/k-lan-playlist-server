package net.kokkeli.data.services;

import java.util.Collection;

import com.google.inject.Inject;

import net.kokkeli.data.ILogger;
import net.kokkeli.data.LogSeverity;
import net.kokkeli.data.Track;
import net.kokkeli.data.db.DatabaseException;
import net.kokkeli.data.db.ITrackDatabase;
import net.kokkeli.server.IFileSystem;

/**
 * Track service.
 * @author Hekku2
 *
 */
public class TrackService implements ITrackService{
    private final ITrackDatabase trackDatabase;
    private final IFileSystem filesystem;
    private final ILogger logger;
    
    /**
     * Creates a track service
     * @param tracks Trackdatabase
     * @param logger Logger
     * @param filesystem The filesystem
     */
    @Inject
    public TrackService(ILogger logger, ITrackDatabase trackDatabase, IFileSystem filesystem){
        this.logger = logger;
        this.trackDatabase = trackDatabase;
        this.filesystem = filesystem;
    }
    
    @Override
    public Collection<Track> getAndVerifyTracks() throws ServiceException {
        try {
            Collection<Track> tracks = trackDatabase.get();
            
            for (Track track : tracks) {
                track.setExists(filesystem.fileExists(track.getLocation()));
            }
            
            return tracks;
        } catch (DatabaseException e) {
            logger.log("Something went wrong in database: " + e.getMessage(), LogSeverity.ERROR);
            throw new ServiceException("There was problem with database.", e);
        }
    }
}
