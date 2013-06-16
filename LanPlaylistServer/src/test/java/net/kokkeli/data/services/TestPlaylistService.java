package net.kokkeli.data.services;

import java.util.ArrayList;
import java.util.Collection;

import net.kokkeli.data.ILogger;
import net.kokkeli.data.PlayList;
import net.kokkeli.data.db.DatabaseException;
import net.kokkeli.data.db.IPlaylistDatabase;
import net.kokkeli.data.db.NotFoundInDatabase;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import static org.mockito.Mockito.*;

public class TestPlaylistService {   
    private IPlaylistDatabase mockDatabase;
    private ILogger mockLogger;
    
    private PlaylistService playlistService;
    
    private final static String EXISTING_PLAYLIST_NAME = "Jarmo";
    
    @Before
    public void setup() throws NotFoundInDatabase, DatabaseException, ServiceException{
        mockDatabase = mock(IPlaylistDatabase.class);
        mockLogger = mock(ILogger.class);
        
        PlayList existingPlaylist = new PlayList(4);
        existingPlaylist.setName(EXISTING_PLAYLIST_NAME);
        
        Collection<PlayList> items = new ArrayList<PlayList>();
        items.add(existingPlaylist);
        
        when(mockDatabase.getOnlyIdAndName()).thenReturn(items);
        
        playlistService = new PlaylistService(mockLogger, mockDatabase);
    }
    
    @Test
    public void testNameExistsReturnsFalseForEmptyName() throws NotFoundInDatabase, DatabaseException, ServiceException{
        Assert.assertFalse(playlistService.nameExists(null));
        Assert.assertFalse(playlistService.nameExists(""));
        Assert.assertFalse(playlistService.nameExists("   "));
    }
    
    @Test
    public void testNameExistsDoesntCareAboutWhiteSpaceInEndOrBeginOfName() throws ServiceException{
        Assert.assertTrue(playlistService.nameExists("     " + EXISTING_PLAYLIST_NAME + "   "));
        Assert.assertTrue(playlistService.nameExists(EXISTING_PLAYLIST_NAME));
    }
    
    @Test
    public void testNameExistsReturnFalseWhenNameDoesntExists() throws ServiceException{
        Assert.assertFalse(playlistService.nameExists("Marsut"));
    }
}
