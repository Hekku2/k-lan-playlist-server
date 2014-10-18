package net.kokkeli.acceptance;

import org.junit.Assert;
import org.junit.Test;

public class TestAuthentication extends BaseAcceptanceTest{
    private final String authenticationPageTitle = "Sign in";
    
    @Test
    public void testAuthenticationPageIsShownOnStart(){
        String url = settings.getURI("").toString();
        driver.get(url);
        Assert.assertEquals(authenticationPageTitle, driver.getTitle());
    }
    
    @Test
    public void testUserIsRedirectedToMainPageAfterAuthentication(){
        authenticate("admin", "kokkeli");
        
        Assert.assertEquals("Main page", driver.getTitle());
    }
    
    @Test
    public void testUsernameIsShownAfterAuthentication(){
        Assert.assertEquals("admin", authenticate("admin", "kokkeli").loggedInUser());
    }
    
    @Test
    public void testUserCantLogInWithWrongPassword(){
        authenticate("admin", "wrong");
        Assert.assertEquals(authenticationPageTitle, driver.getTitle());
    }
}
