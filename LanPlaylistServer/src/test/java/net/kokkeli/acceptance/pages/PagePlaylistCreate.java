package net.kokkeli.acceptance.pages;

import net.kokkeli.settings.ISettings;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class PagePlaylistCreate extends BasePage{

    public PagePlaylistCreate(ISettings settings, WebDriver driver) {
        super(settings, driver, "playlists/create");
    }

    public void createPlaylist(String name){
        driver.findElement(By.name("name")).sendKeys(name);
        driver.findElement(By.cssSelector("input[type='submit']")).click();
    }
}
