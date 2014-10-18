package net.kokkeli.acceptance;

import net.kokkeli.acceptance.pages.PageAuthentication;

import org.junit.Assert;
import org.junit.Test;

public class TestAuthentication extends BaseAcceptanceTest{
    
    @Test
    public void testAuthenticationPageIsShownOnStart(){
        String url = settings.getURI("").toString();
        driver.get(url);
        Assert.assertEquals("Sign in", driver.getTitle());
    }
    
    @Test
    public void testUserIsRedirectedToMainPageAfterAuthentication(){
        PageAuthentication page = new PageAuthentication(settings, driver);
        page.Open();
        page.LogIn("admin", "kokkeli");
        
        Assert.assertEquals("Main page", driver.getTitle());
    }
    
    @Test
    public void testUsernameIsShownAfterAuthentication(){
        PageAuthentication page = new PageAuthentication(settings, driver);
        page.Open();
        page.LogIn("admin", "kokkeli");
        
        Assert.assertEquals("admin", page.LoggedInUser());
    }
}
