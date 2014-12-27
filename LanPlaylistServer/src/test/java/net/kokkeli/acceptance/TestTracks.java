package net.kokkeli.acceptance;

import net.kokkeli.acceptance.pages.PageTracks;

import org.junit.Assert;
import org.junit.Test;

public class TestTracks extends BaseAcceptanceTest {
    
    @Test
    public void testTrackPagesHasListOfTracks(){
        authenticate(ADMIN_USERNAME, DEFAULT_PASSWORD);
        
        PageTracks trackPage = new PageTracks(settings, driver);
        trackPage.open();
        
        Assert.assertTrue(trackPage.getTracks().size() > 0);
    }
}