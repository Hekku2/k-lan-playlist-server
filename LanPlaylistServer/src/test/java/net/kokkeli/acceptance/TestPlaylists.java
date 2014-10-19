package net.kokkeli.acceptance;

import net.kokkeli.acceptance.pages.PagePlaylistCreate;
import net.kokkeli.acceptance.pages.PagePlaylists;
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
        
        PagePlaylistCreate page = new PagePlaylistCreate(settings, driver);
        page.open();
        page.createPlaylist("New playlist");
        
        listPage = new PagePlaylists(settings, driver);
        listPage.open();
        Assert.assertEquals(playlists + 1, listPage.playlistCount());
    }
}
