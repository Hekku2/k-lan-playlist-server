package net.kokkeli.acceptance.pages;

import net.kokkeli.ISettings;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class PageAuthentication extends BasePage{
    
    public PageAuthentication(ISettings settings, WebDriver driver){
        super(settings, driver, "authentication");
    }
    
    public void LogIn(String username, String password){
        driver.findElement(By.id("user")).sendKeys(username);
        driver.findElement(By.id("pwd")).sendKeys(password);
        driver.findElement(By.cssSelector("button[type='submit']")).click();
    }
}
