package net.kokkeli.acceptance.pages;

import java.util.ArrayList;
import java.util.List;

import net.kokkeli.ISettings;
import net.kokkeli.data.Role;
import net.kokkeli.resources.models.ModelUser;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

public class PageUsers extends BasePage{

    public PageUsers(ISettings settings, WebDriver driver) {
        super(settings, driver, "users");
    }

    public ArrayList<ModelUser> getUsers(){
        List<WebElement> rows = driver.findElements(By.cssSelector("tbody tr"));
        ArrayList<ModelUser> users = new ArrayList<ModelUser>();
        for (WebElement row : rows) {
            String username = row.findElement(By.cssSelector("td:nth-child(1)")).getText();
            String role = row.findElement(By.cssSelector("td:nth-child(2)")).getText();
            
            ModelUser user = new ModelUser();
            user.setUsername(username);
            user.setRole(Role.valueOf(role));
            users.add(user);
        }
        return users;
    }
}
