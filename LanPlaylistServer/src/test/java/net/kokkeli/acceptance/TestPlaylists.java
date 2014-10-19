package net.kokkeli.acceptance;

import net.kokkeli.acceptance.pages.PagePlaylist;
import net.kokkeli.acceptance.pages.PagePlaylistCreate;
import net.kokkeli.acceptance.pages.PagePlaylists;
import net.kokkeli.resources.models.ModelPlaylist;
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
        
        PagePlaylist playlist = new PagePlaylist(settings, driver);
        ModelPlaylist created = playlist.getPlaylist();
        Assert.assertEquals("New playlist", created.getName());
        
        listPage = new PagePlaylists(settings, driver);
        listPage.open();
        Assert.assertEquals(playlists + 1, listPage.playlistCount());
    }
}
