package net.kokkeli.acceptance.pages;

import java.util.ArrayList;
import java.util.List;

import net.kokkeli.resources.models.ModelPlaylistItem;
import net.kokkeli.settings.ISettings;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

public class PageIndex extends BasePage{

    public PageIndex(ISettings settings, WebDriver driver) {
        super(settings, driver, "index");
    }

    public ArrayList<ModelPlaylistItem> getPlaylistItems(){
        ArrayList<ModelPlaylistItem> items = new ArrayList<ModelPlaylistItem>();
        
        List<WebElement> rows = driver.findElements(By.cssSelector("tbody tr"));
        for (WebElement row : rows) {
            String artist = row.findElement(By.cssSelector("td:nth-child(1)")).getText();
            String track = row.findElement(By.cssSelector("td:nth-child(2)")).getText();
            
            ModelPlaylistItem item = new ModelPlaylistItem();
            item.setArtist(artist);
            item.setTrack(track);
            items.add(item);
        }
        
        return items;
    }
    
    public void removeTrack(int trackId){
        WebElement element = driver.findElement(By.cssSelector("button[data-method='delete'][data-track-id='"+ trackId +"']"));
        element.click();
        //TODO Implement better remove function so state can be checked.
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
