package net.kokkeli.acceptance;

import java.util.ArrayList;

import net.kokkeli.acceptance.pages.PageUsers;
import net.kokkeli.resources.models.ModelUser;

import org.junit.Assert;
import org.junit.Test;

public class TestUserManagement extends BaseAcceptanceTest{

    @Test
    public void testUserListIsShownWithCorrectUsers(){
        authenticate("admin", "kokkeli");
        
        PageUsers page = new PageUsers(settings, driver);
        page.open();
        ArrayList<ModelUser> users = page.getUsers();
        
        Assert.assertTrue("There should be at least admin user.", users.size() >= 1);
    }
}
