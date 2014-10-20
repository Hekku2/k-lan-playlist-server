package net.kokkeli.acceptance;

import net.kokkeli.acceptance.pages.PageManagement;

import org.junit.Assert;
import org.junit.Test;

public class TestManagement extends BaseAcceptanceTest{

    @Test
    public void testManagementPageIsShown(){
        authenticate(ADMIN_USERNAME, DEFAULT_PASSWORD);
        
        PageManagement page = new PageManagement(settings, driver);
        page.open();
        
        Assert.assertEquals("Management", driver.getTitle());
    }
}
