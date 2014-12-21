package net.kokkeli.acceptance;

import net.kokkeli.acceptance.pages.PageIndex;
import net.kokkeli.acceptance.pages.PagePlaylists;

import org.junit.Assert;
import org.junit.Test;

public class TestIndex extends BaseAcceptanceTest{
    
    @Test
    public void TestSelectedPlaylistIsShownOnIndexPage(){
        authenticate(ADMIN_USERNAME, DEFAULT_PASSWORD);
        selectDefaultPlaylist();
        
        //Navigate to index page
        PageIndex index = new PageIndex(settings, driver);
        index.open();
        Assert.assertTrue("There should have been at least 1 playlist item, probably more.", index.getPlaylistItems().size() > 0);
    }
    
    @Test
    public void TestPlaylistItemsCanBeRemovedByAdminFromIndexPage(){
        authenticate(ADMIN_USERNAME, DEFAULT_PASSWORD);
        selectDefaultPlaylist();
        
        //Navigate to index page
        PageIndex index = new PageIndex(settings, driver);
        index.open();
        int size = index.getPlaylistItems().size();
        index.removeTrack(TEST_TRACK_ID);
        index.open();
        Assert.assertEquals(size - 1 , index.getPlaylistItems().size());
    }
    
    private void selectDefaultPlaylist(){
        PagePlaylists playlists = new PagePlaylists(settings, driver);
        playlists.open();
        playlists.selectFirstPlaylist();
    }
}
