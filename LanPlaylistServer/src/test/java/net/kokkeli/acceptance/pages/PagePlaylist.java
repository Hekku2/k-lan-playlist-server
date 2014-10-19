package net.kokkeli.acceptance.pages;

import net.kokkeli.ISettings;
import net.kokkeli.resources.models.ModelPlaylist;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class PagePlaylist extends BasePage{

    public PagePlaylist(ISettings settings, WebDriver driver) {
        super(settings, driver, "/playlists");
    }

    public ModelPlaylist getPlaylist(){
        String currentUrl = driver.getCurrentUrl();
        String id = currentUrl.substring(currentUrl.lastIndexOf("/")+1);
        
        ModelPlaylist model = new ModelPlaylist(Integer.parseInt(id));
        model.setName(driver.findElement(By.id("name")).getText());
        return model;
    }
}
