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
        authenticate(ADMIN_USERNAME, DEFAULT_PASSWORD);
        
        Assert.assertEquals("Main page", driver.getTitle());
    }
    
    @Test
    public void testUsernameIsShownAfterAuthentication(){
        Assert.assertEquals(ADMIN_USERNAME, authenticate(ADMIN_USERNAME, DEFAULT_PASSWORD).loggedInUser());
    }
    
    @Test
    public void testUserCantLogInWithWrongPassword(){
        authenticate(ADMIN_USERNAME, "wrong");
        Assert.assertEquals(authenticationPageTitle, driver.getTitle());
    }
}
