package net.kokkeli.acceptance.pages;

import net.kokkeli.resources.models.ModelFetchRequestCreate;
import net.kokkeli.settings.ISettings;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.Select;

public class PageCreateFetchRequest extends BasePage{

    public PageCreateFetchRequest(ISettings settings, WebDriver driver) {
        super(settings, driver, "fetchers/createRequest");
    }

    public void create(ModelFetchRequestCreate newFetchRequest) {
        driver.findElement(By.name("handler")).sendKeys(newFetchRequest.getHandler());
        driver.findElement(By.name("location")).sendKeys(newFetchRequest.getLocation());
        driver.findElement(By.name("destination")).sendKeys(newFetchRequest.getDestination());
        driver.findElement(By.name("artist")).sendKeys(newFetchRequest.getArtist());
        driver.findElement(By.name("track")).sendKeys(newFetchRequest.getTrack());
        new Select(driver.findElement(By.name("selectedplaylistid"))).selectByValue(newFetchRequest.getSelectedPlaylistId()+"");
        
        driver.findElement(By.cssSelector("input[type='submit']")).click();
    }

}
