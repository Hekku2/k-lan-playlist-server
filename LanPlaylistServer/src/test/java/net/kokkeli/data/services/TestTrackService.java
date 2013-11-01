package net.kokkeli.data.services;

import static org.mockito.Mockito.mock;

import java.util.ArrayList;
import net.kokkeli.data.ILogger;
import net.kokkeli.data.Track;
import net.kokkeli.data.db.DatabaseException;
import net.kokkeli.data.db.ITrackDatabase;
import net.kokkeli.data.db.NotFoundInDatabase;
import net.kokkeli.server.IFileSystem;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import static org.mockito.Mockito.*;

public class TestTrackService {
    private ITrackDatabase mockDatabase;
    private ILogger mockLogger;
    private IFileSystem mockFileSystem;
    
    private TrackService trackService;
    
    @Before
    public void setup(){
        mockDatabase = mock(ITrackDatabase.class);
        mockLogger = mock(ILogger.class);
        mockFileSystem = mock(IFileSystem.class);
        
        trackService = new TrackService(mockLogger, mockDatabase, mockFileSystem);
    }
    
    @Test
    public void testGetAndVerifyTrackThrowsServiceExceptionWhenDatabaseExceptionIsThrown() throws DatabaseException {
        when(mockDatabase.get()).thenThrow(new DatabaseException("Explosion."));
        
        try {
            trackService.getAndVerifyTracks();
        } catch (ServiceException e) {
            Assert.assertEquals("There was problem with database.", e.getMessage());
        }
    }
    
    @Test
    public void testGetAndVerifyVerifiesTracks() throws DatabaseException, ServiceException{
        ArrayList<Track> tracks = new ArrayList<Track>();
        for (int i = 0; i < 3; i++) {
            Track track = new Track();
            track.setArtist("Artist! " + i);
            track.setTrackName("TrackName" + i);
            track.setLocation("Location"+i);
            track.setId(i);
            tracks.add(track);
        }
        
        when(mockDatabase.get()).thenReturn(tracks);
        when(mockFileSystem.fileExists("Location1")).thenReturn(true);
        
        Track[] verified = new Track[3];
        trackService.getAndVerifyTracks().toArray(verified);
        
        Assert.assertFalse(verified[0].getExists());
        Assert.assertTrue(verified[1].getExists());
        Assert.assertFalse(verified[2].getExists());
        
        for (int i = 0; i < tracks.size(); i++) {
            Assert.assertEquals(verified[i].getArtist(), tracks.get(i).getArtist());
            Assert.assertEquals(verified[i].getTrackName(), tracks.get(i).getTrackName());
            Assert.assertEquals(verified[i].getUploader(), tracks.get(i).getUploader());
            Assert.assertEquals(verified[i].getId(), tracks.get(i).getId());
            Assert.assertEquals(verified[i].getLocation(), tracks.get(i).getLocation());
        }
    }
    
    @Test
    public void testGetReturnsCorrectTrack() throws DatabaseException, NotFoundInDatabase, ServiceException {
        Track track = new Track();
        track.setId(5);
        
        when(mockDatabase.get(track.getId())).thenReturn(track);
        
        Track gotten = trackService.get(track.getId());
        Assert.assertEquals(track.getId(), gotten.getId());
    }
    
    @Test
    public void testGetThrowsServiceExceptionWhenDatabaseThrowsException() throws DatabaseException, NotFoundInDatabase{
        when(mockDatabase.get(anyLong())).thenThrow(new DatabaseException("Räjähti!"));
        try {
            trackService.get(5);
        } catch (ServiceException e) {
            Assert.assertEquals("There was problem with database.", e.getMessage());
        }
    }
}
