package net.kokkeli.resources;

import java.io.IOException;
import java.io.InputStream;

import javax.ws.rs.core.Response;

import net.kokkeli.ISettings;
import net.kokkeli.data.db.NotFoundInDatabase;
import net.kokkeli.data.db.PlayList;
import net.kokkeli.data.services.IPlaylistService;
import net.kokkeli.data.services.ServiceException;
import net.kokkeli.resources.models.BaseModel;
import net.kokkeli.server.IFileSystem;
import net.kokkeli.server.NotAuthenticatedException;
import net.kokkeli.server.RenderException;

import org.junit.Assert;
import org.junit.Test;

import com.sun.jersey.core.header.FormDataContentDisposition;

import static org.mockito.Mockito.*;

public class TestPlaylistResource extends ResourceTestsBase {
    private IFileSystem mockFilesystem;
    private ISettings mockSettings;
    private IPlaylistService mockPlaylistService;
    
    private PlaylistsResource resource;
    
    private final long EXISTING_PLAYLIST = 654;
    private final String TRACKS_FOLDER = "trackfolder";
    
    private final String CORRECT_TRACKNAME = "Humppajuna 666";
    private final String CORRECT_ARTISTNAME = "Humppupumppu";
    
    @Override
    public void before() throws Exception {
        mockFilesystem = mock(IFileSystem.class);
        mockSettings = mock(ISettings.class);
        mockPlaylistService = mock(IPlaylistService.class);
        
        PlayList playlist = new PlayList(EXISTING_PLAYLIST);
        
        when(mockPlaylistService.getPlaylist(EXISTING_PLAYLIST)).thenReturn(playlist);
        when(mockSettings.getTracksFolder()).thenReturn(TRACKS_FOLDER);
        
        resource = new PlaylistsResource(getLogger(), getTemplateService(), getPlayer(), getSessionService(), mockSettings, mockPlaylistService, mockFilesystem);
    }
    
    @Test
    public void testAddPostWithProperValueTriesToWriteToFile() throws ServiceException, NotFoundInDatabase, NotAuthenticatedException, IOException{
        final String filename = "filename";
        
        InputStream mockStream = mock(InputStream.class);
        FormDataContentDisposition mockDisposition = mock(FormDataContentDisposition.class);
        when(mockDisposition.getFileName()).thenReturn(filename);
        
        Response r = resource.add(buildRequest(), EXISTING_PLAYLIST, CORRECT_ARTISTNAME, CORRECT_TRACKNAME, mockStream, mockDisposition);
        Assert.assertEquals(RESPONSE_OK, r.getStatus());
        verify(mockFilesystem).writeToFile(any(InputStream.class), eq(TRACKS_FOLDER + "/" + filename));
        verify(mockFilesystem).fileExists(TRACKS_FOLDER + "/" + filename);
    }
    
    @Test
    public void testAddPostReturnsErrorWhenFileExists() throws ServiceException, NotFoundInDatabase, NotAuthenticatedException, RenderException{
        ModelAnswer answer = new ModelAnswer();
        when(getTemplateService().process(any(String.class), any(BaseModel.class))).thenAnswer(answer);
        
        final String filename = "filename";
        InputStream mockStream = mock(InputStream.class);
        FormDataContentDisposition mockDisposition = mock(FormDataContentDisposition.class);
        when(mockDisposition.getFileName()).thenReturn(filename);
        when(mockFilesystem.fileExists(any(String.class))).thenReturn(true);
        
        assertModelResponse(resource.add(buildRequest(),
                EXISTING_PLAYLIST, CORRECT_ARTISTNAME, CORRECT_TRACKNAME, mockStream, mockDisposition), answer,
                "Similar file already exists. Remove existing file, or upload different.", null);
    }
}
