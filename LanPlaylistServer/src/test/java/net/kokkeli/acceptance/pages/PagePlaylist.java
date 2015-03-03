package net.kokkeli.acceptance.pages;

import java.util.List;

import net.kokkeli.resources.models.ModelPlaylist;
import net.kokkeli.resources.models.ModelPlaylistItem;
import net.kokkeli.settings.ISettings;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

public class PagePlaylist extends BasePage{

    public PagePlaylist(ISettings settings, WebDriver driver, long id) {
        super(settings, driver, "playlists/" + id);
    }

    public ModelPlaylist getPlaylist(){
        String currentUrl = driver.getCurrentUrl();
        String id = currentUrl.substring(currentUrl.lastIndexOf("/")+1);
        
        ModelPlaylist model = new ModelPlaylist(Integer.parseInt(id));
        model.setName(driver.findElement(By.id("name")).getText());
        
        List<WebElement> rows = driver.findElements(By.cssSelector("tbody tr"));
        for (WebElement row : rows) {
            ModelPlaylistItem item = new ModelPlaylistItem();
            item.setArtist(row.findElement(By.cssSelector("td:nth-child(1)")).getText());
            item.setTrack(row.findElement(By.cssSelector("td:nth-child(2)")).getText());
            item.setUploader(row.findElement(By.cssSelector("td:nth-child(3)")).getText());
            model.getItems().add(item);
        }
        
        return model;
    }

    public void removeTrack(int trackId) {
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
