package net.kokkeli.acceptance;

import org.junit.Assert;
import org.junit.Test;

public class TestAuthentication extends BaseAcceptanceTest{
    
    @Test
    public void testAuthenticationPageIsShownOnStart(){
        String url = settings.getURI("").toString();
        driver.get(url);
        Assert.assertEquals("Sign in", driver.getTitle());
    }
}
