package net.kokkeli.acceptance.pages;

import net.kokkeli.ISettings;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public abstract class BasePage {
    private By usernameSelector = By.cssSelector(".navbar a[href*='/users/']");
    
    protected final WebDriver driver;
    protected final ISettings settings;
    
    protected final String url;
    
    protected BasePage(ISettings settings, WebDriver driver, String pageUrl){
        this.driver = driver;
        this.settings = settings;
        this.url = pageUrl;
    }
    
    public void open(){
        driver.get(settings.getURI(url).toString());
    }
    
    public String loggedInUser(){
        return driver.findElement(usernameSelector).getText();
    }
    
    public String getAlert(){
        return driver.findElement(By.cssSelector("div[role='alert']")).getText();
    }

    public void clickUsername() {
        driver.findElement(usernameSelector).click();
    }
}
