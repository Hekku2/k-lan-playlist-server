package net.kokkeli.acceptance;

import java.util.ArrayList;

import net.kokkeli.acceptance.pages.PageUser;
import net.kokkeli.acceptance.pages.PageUserCreate;
import net.kokkeli.acceptance.pages.PageUsers;
import net.kokkeli.data.Role;
import net.kokkeli.resources.models.ModelUser;

import org.junit.Assert;
import org.junit.Test;

public class TestUserManagement extends BaseAcceptanceTest{
    private int adminId = 1;
    
    @Test
    public void testUserListIsShownWithCorrectUsers(){
        authenticate(ADMIN_USERNAME, DEFAULT_PASSWORD);
        
        PageUsers page = new PageUsers(settings, driver);
        page.open();
        ArrayList<ModelUser> users = page.getUsers();
        
        Assert.assertTrue("There should be at least admin user.", users.size() >= 1);
    }
    
    @Test
    public void testUserCanBeCreated(){
        authenticate(ADMIN_USERNAME, DEFAULT_PASSWORD);
        
        PageUsers userList = new PageUsers(settings, driver);
        userList.open();
        int oldCount =  userList.getUsers().size();
        
        ModelUser newUser = new ModelUser(0, "newUser", Role.ADMIN);
        newUser.setNewPassword("password");
        newUser.setConfirmPassword("password");
        
        PageUserCreate page = new PageUserCreate(settings, driver);
        page.open();
        page.createUser(newUser);
        
        userList.open();
        Assert.assertEquals(oldCount + 1, userList.getUsers().size());
    }
    
    @Test
    public void testUserPageShowsCorrectInformation(){
        authenticate(ADMIN_USERNAME, DEFAULT_PASSWORD);
        
        PageUser page = new PageUser(settings, driver);
        page.Open(adminId);
        ModelUser user = page.getUser();
        Assert.assertEquals(ADMIN_USERNAME, user.getUsername());
        Assert.assertEquals("ADMIN", user.getRole());
    }
    
    @Test
    public void testCreatedUserCanLogIn(){
        authenticate(ADMIN_USERNAME, DEFAULT_PASSWORD);
        
        ModelUser newUser = new ModelUser(0, "newUser", Role.ADMIN);
        newUser.setNewPassword("password");
        newUser.setConfirmPassword("password");
        
        PageUserCreate page = new PageUserCreate(settings, driver);
        page.open();
        page.createUser(newUser);
        
        logOut();
        Assert.assertEquals("newUser", authenticate("newUser", "password").loggedInUser());
    }
}
