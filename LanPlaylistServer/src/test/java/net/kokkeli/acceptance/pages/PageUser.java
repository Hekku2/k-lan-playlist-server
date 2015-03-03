package net.kokkeli.acceptance.pages;

import net.kokkeli.data.dto.Role;
import net.kokkeli.resources.models.ModelUser;
import net.kokkeli.settings.ISettings;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class PageUser extends BasePage {

    public PageUser(ISettings settings, WebDriver driver) {
        super(settings, driver, "users");
    }
    
    public void Open(long id){
        driver.get(settings.getURI(url + "/" + id).toString());
    }

    public ModelUser getUser(){
        ModelUser user = new ModelUser();
        user.setUsername(driver.findElement(By.id("username")).getText());
        user.setRole(Role.valueOf(driver.findElement(By.id("role")).getText()));
        return user;
    }
}
