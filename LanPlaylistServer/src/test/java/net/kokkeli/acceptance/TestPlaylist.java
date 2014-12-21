package net.kokkeli.acceptance;

import net.kokkeli.acceptance.pages.PagePlaylist;
import net.kokkeli.resources.models.ModelPlaylist;

import org.junit.Assert;
import org.junit.Test;

public class TestPlaylist extends BaseAcceptanceTest{

    @Test
    public void TestAdminCanRemoveItemsFromPlaylist(){
        authenticate(ADMIN_USERNAME, DEFAULT_PASSWORD);
        
        PagePlaylist playlistPage = new PagePlaylist(settings, driver, TEST_PLAYLIST_ID);
        playlistPage.open();
        ModelPlaylist oldList = playlistPage.getPlaylist();
        
        playlistPage.removeTrack(TEST_TRACK_ID);
        playlistPage.open();
        ModelPlaylist updatedList = playlistPage.getPlaylist();
        Assert.assertEquals(oldList.getItems().size() - 1, updatedList.getItems().size());
    }
}