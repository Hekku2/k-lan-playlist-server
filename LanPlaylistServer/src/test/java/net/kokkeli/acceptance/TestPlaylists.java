package net.kokkeli.acceptance;

import net.kokkeli.acceptance.pages.PagePlaylist;
import net.kokkeli.acceptance.pages.PagePlaylistAdd;
import net.kokkeli.acceptance.pages.PagePlaylistCreate;
import net.kokkeli.acceptance.pages.PagePlaylists;
import net.kokkeli.resources.models.ModelPlaylist;
import net.kokkeli.resources.models.ModelPlaylistItem;
import net.kokkeli.resources.models.ModelPlaylists;

import org.junit.Assert;
import org.junit.Test;

public class TestPlaylists extends BaseAcceptanceTest{
    
    @Test
    public void testListViewShowsItems(){
        authenticate(ADMIN_USERNAME, DEFAULT_PASSWORD);
        
        PagePlaylists page = new PagePlaylists(settings, driver);
        page.open();
        ModelPlaylists list = page.getPlaylists();
        Assert.assertEquals(2, list.getItems().size());
    }
    
    @Test
    public void testPlaylistCanBeCreated(){
        authenticate(ADMIN_USERNAME, DEFAULT_PASSWORD);
        
        PagePlaylists listPage = new PagePlaylists(settings, driver);
        listPage.open();
        int playlists = listPage.playlistCount();
        
        PagePlaylistCreate create = new PagePlaylistCreate(settings, driver);
        create.open();
        create.createPlaylist("New playlist");
        
        PagePlaylist playlist = new PagePlaylist(settings, driver, 1);
        ModelPlaylist created = playlist.getPlaylist();
        Assert.assertEquals("New playlist", created.getName());
        
        listPage = new PagePlaylists(settings, driver);
        listPage.open();
        Assert.assertEquals(playlists + 1, listPage.playlistCount());
    }
    
    @Test
    public void testAddingVlcUpload(){
        authenticate(ADMIN_USERNAME, DEFAULT_PASSWORD);
        
        ModelPlaylistItem newItem = new ModelPlaylistItem();
        newItem.setArtist("test artist");
        newItem.setTrack("test track");
        newItem.setUrl("http://localhost/");
        
        PagePlaylist playlist = new PagePlaylist(settings, driver, TEST_PLAYLIST_ID);
        playlist.open();
        int oldSize = playlist.getPlaylist().getItems().size();
        
        PagePlaylistAdd addPage = new PagePlaylistAdd(settings, driver, TEST_PLAYLIST_ID);
        addPage.open();
        addPage.openVlcMethod();
        addPage.addItem(newItem);
        
        playlist.open();
        Assert.assertEquals(oldSize + 1, playlist.getPlaylist().getItems().size());
    }
}
