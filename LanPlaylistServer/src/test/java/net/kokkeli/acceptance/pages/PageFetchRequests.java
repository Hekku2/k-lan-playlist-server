package net.kokkeli.acceptance.pages;

import java.util.List;

import net.kokkeli.ISettings;
import net.kokkeli.data.Track;
import net.kokkeli.resources.models.ModelFetchRequest;
import net.kokkeli.resources.models.ModelFetchRequests;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

public class PageFetchRequests extends BasePage{
    private final String trackNameSeparator = " - ";
    
    public PageFetchRequests(ISettings settings, WebDriver driver) {
        super(settings, driver, "fetchers");
    }

    public ModelFetchRequests getFetchRequests(){
        ModelFetchRequests model = new ModelFetchRequests();
        
        List<WebElement> rows = driver.findElements(By.cssSelector("tbody tr"));
        for (WebElement row : rows) {
            ModelFetchRequest item = new ModelFetchRequest();
            item.setHandler(row.findElement(By.cssSelector("td:nth-child(1)")).getText());
            
            String name = row.findElement(By.cssSelector("td:nth-child(2)")).getText();
            
            Track track = new Track();
            track.setArtist(name.substring(0, name.indexOf(trackNameSeparator)));
            track.setTrackName(name.substring(name.indexOf(trackNameSeparator) + trackNameSeparator.length()));
            
            item.setTrack(track);
            item.setLocation(row.findElement(By.cssSelector("td:nth-child(3)")).getText());
            item.setDestination(row.findElement(By.cssSelector("td:nth-child(4)")).getText());
            
            model.getItems().add(item);
        }
        
        return model;
    }
}
