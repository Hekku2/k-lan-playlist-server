package net.kokkeli.acceptance.pages;

import java.util.ArrayList;
import java.util.List;

import net.kokkeli.resources.models.ModelTrack;
import net.kokkeli.settings.ISettings;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

public class PageTracks extends BasePage{

    public PageTracks(ISettings settings, WebDriver driver) {
        super(settings, driver, "tracks");
    }

    public ArrayList<ModelTrack> getTracks(){
        ArrayList<ModelTrack> tracks = new ArrayList<ModelTrack>();
        
        List<WebElement> rows = driver.findElements(By.cssSelector("tbody tr"));
        for (WebElement row : rows) {
            String artist = row.findElement(By.cssSelector("td:nth-child(1)")).getText();
            String trackName = row.findElement(By.cssSelector("td:nth-child(2)")).getText();
            
            ModelTrack track = new ModelTrack();
            track.setArtist(artist);
            track.setTrack(trackName);
            tracks.add(track);
        }
        return tracks;
    }
    
}
