package net.kokkeli.acceptance.pages;

import java.util.List;

import net.kokkeli.resources.models.ModelUser;
import net.kokkeli.settings.ISettings;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

public class PageUserCreate extends BasePage{

    public PageUserCreate(ISettings settings, WebDriver driver) {
        super(settings, driver, "users/create");
    }

    public void createUser(ModelUser user){
        driver.findElement(By.name("username")).sendKeys(user.getUsername());
        List<WebElement> roleSelections = driver.findElements(By.name("role"));
        for (WebElement webElement : roleSelections) {
            if (webElement.getAttribute("value").equals(user.getRoleEnum().getId() + ""))
                webElement.click();
        }
        
        driver.findElement(By.name("newpassword")).sendKeys(user.getNewPassword());
        driver.findElement(By.name("confirmpassword")).sendKeys(user.getConfirmPassword());
        driver.findElement(By.cssSelector("input[type='submit']")).click();
    }
}
