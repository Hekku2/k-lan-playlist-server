package net.kokkeli.acceptance.pages;

import java.util.List;

import net.kokkeli.resources.models.ModelPlaylist;
import net.kokkeli.resources.models.ModelPlaylists;
import net.kokkeli.settings.ISettings;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

public class PagePlaylists extends BasePage{

    public PagePlaylists(ISettings settings, WebDriver driver) {
        super(settings, driver, "playlists");
    }

    public ModelPlaylists getPlaylists(){
        ModelPlaylists lists = new ModelPlaylists();
        
        List<WebElement> rows = driver.findElements(By.cssSelector("tbody tr"));
        for (WebElement row : rows) {
            int id = Integer.parseInt(row.findElement(By.cssSelector("td:nth-child(2) button")).getAttribute("id"));
            ModelPlaylist playlist = new ModelPlaylist(id);
            playlist.setName(row.findElement(By.cssSelector("td:nth-child(1)")).getText());
            lists.getItems().add(playlist);
        }
        
        return lists;
    }
    
    public int playlistCount(){
        return driver.findElements(By.cssSelector("tbody tr")).size();
    }

    public void selectFirstPlaylist() {
        By selector = By.cssSelector("tbody button.select-playlist");
        WebElement button = driver.findElement(selector);
        button.click();
    }
}
