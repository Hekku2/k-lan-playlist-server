package net.kokkeli.acceptance;

import net.kokkeli.acceptance.pages.PageCreateFetchRequest;
import net.kokkeli.acceptance.pages.PageFetchRequests;
import net.kokkeli.resources.models.ModelFetchRequest;
import net.kokkeli.resources.models.ModelFetchRequestCreate;
import net.kokkeli.resources.models.ModelFetchRequests;

import org.junit.Assert;
import org.junit.Test;

public class TestFetchRequests extends BaseAcceptanceTest{
    
     @Test
     public void testAddingFetchRequests(){
         authenticate(ADMIN_USERNAME, DEFAULT_PASSWORD);
         
         ModelFetchRequestCreate newFetchRequest = new ModelFetchRequestCreate();
         newFetchRequest.setHandler("mock handler");
         newFetchRequest.setLocation("location");
         newFetchRequest.setDestination("destination");
         newFetchRequest.setArtist("artist");
         newFetchRequest.setTrack("track");
         newFetchRequest.setSelectedPlaylistId(TEST_PLAYLIST_ID);
         
         PageFetchRequests fetchRequests = new PageFetchRequests(settings, driver);
         fetchRequests.open();
         int requests = fetchRequests.getFetchRequests().getItems().size();
         
         PageCreateFetchRequest createPage = new PageCreateFetchRequest(settings, driver);
         createPage.open();
         createPage.create(newFetchRequest);
         
         Assert.assertEquals("Fetch request created.", createPage.getAlert());
         
         fetchRequests.open();
         ModelFetchRequests request = fetchRequests.getFetchRequests();
         ModelFetchRequest latest = request.getItems().get(requests);
         
         Assert.assertEquals(newFetchRequest.getArtist() + " - " + newFetchRequest.getTrack(), latest.getTrack());
         Assert.assertEquals(newFetchRequest.getHandler(), latest.getHandler());
         Assert.assertEquals(newFetchRequest.getLocation(), latest.getLocation());
         Assert.assertEquals(newFetchRequest.getDestination(), latest.getDestination());
     }
}
