package net.kokkeli.acceptance.pages;

import net.kokkeli.ISettings;
import net.kokkeli.resources.models.ModelPlaylistItem;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class PagePlaylistAdd extends BasePage{

    public PagePlaylistAdd(ISettings settings, WebDriver driver, long id) {
        super(settings, driver, "playlists/add/upload/" + id);
    }

    public void openVlcMethod() {
        driver.findElement(By.id("vlc")).click();
    }

    public void addItem(ModelPlaylistItem item) {
        driver.findElement(By.name("artist")).sendKeys(item.getArtist());
        driver.findElement(By.name("track")).sendKeys(item.getTrack());
        driver.findElement(By.name("url")).sendKeys(item.getUrl());
        driver.findElement(By.cssSelector("input[type='submit']")).click();
    }

}
