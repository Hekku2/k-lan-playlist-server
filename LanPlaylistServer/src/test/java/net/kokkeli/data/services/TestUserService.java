package net.kokkeli.data.services;

import net.kokkeli.data.ILogger;
import net.kokkeli.data.Role;
import net.kokkeli.data.User;
import net.kokkeli.data.db.DatabaseException;
import net.kokkeli.data.db.IUserDatabase;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.sun.jersey.api.NotFoundException;

import static org.mockito.Mockito.*;

public class TestUserService {
    private final static long NON_EXISTING_USER_ID = 4;
    private final static long USER_ID = 666;
    private final static String USERNAME = "Jarmo";
    private final static Role USERROLE = Role.USER;
    
    private IUserDatabase mockDatabase;
    private ILogger mockLogger;
    
    private UserService userService;
    
    @Before
    public void setup() throws NotFoundException, DatabaseException{
        mockDatabase = mock(IUserDatabase.class);
        mockLogger = mock(ILogger.class);
        when(mockDatabase.get(USER_ID)).thenReturn(new User(USER_ID, USERNAME, USERROLE));
        
        userService = new UserService(mockLogger, mockDatabase);
    }
    
    @Test
    public void testUserServiceGetThrowsNotFoundExceptionWhenThereIsNoUser() throws NotFoundException, DatabaseException, ServiceException{
        when(mockDatabase.get(NON_EXISTING_USER_ID)).thenThrow(new NotFoundException("User not found."));
        
        try {
            userService.get(NON_EXISTING_USER_ID);
            Assert.fail("NotFoundException was not thrown.");
        } catch (NotFoundException e) {
            // This should happen
        }
    }
    
    @Test
    public void testUserServiceGetReturnsCorrectUser() throws NotFoundException, ServiceException{
        User user = userService.get(USER_ID);
        
        Assert.assertNotNull(user);
        Assert.assertEquals(USER_ID, user.getId());
        Assert.assertEquals(USERNAME, user.getUserName());
        Assert.assertEquals(USERROLE, user.getRole());
    }
}
