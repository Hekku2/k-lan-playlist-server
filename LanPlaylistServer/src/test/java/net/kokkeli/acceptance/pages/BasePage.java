package net.kokkeli.acceptance.pages;

import net.kokkeli.ISettings;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public abstract class BasePage {
    protected final WebDriver driver;
    protected final ISettings settings;
    
    private final String url;
    
    protected BasePage(ISettings settings, WebDriver driver, String pageUrl){
        this.driver = driver;
        this.settings = settings;
        this.url = pageUrl;
    }
    
    public void open(){
        driver.get(settings.getURI(url).toString());
    }
    
    public String loggedInUser(){
        return driver.findElement(By.cssSelector("p.navbar-right")).getText();
    }
}
