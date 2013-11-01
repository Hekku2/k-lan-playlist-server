package net.kokkeli.data;

import org.junit.Assert;
import org.junit.Test;

public class TestUser {
    @SuppressWarnings("static-method")
    @Test
    public void testUserEquals(){
        final String username = "taisto";
        final long id = 4;
        final Role role = Role.USER;
        
        final String differentUsername = "tiesto";
        final long differentId = 55;
        
        Assert.assertEquals(true, new User(id, username, role).equals(new User(id, username, role)));
        Assert.assertEquals(false, new User(id, username, role).equals(new User(id, differentUsername, role)));
        Assert.assertEquals(false, new User(id, username, role).equals(new User(differentId, username, role)));
        Assert.assertEquals(false, new User(id, username, role).equals(new User(id, username, Role.ADMIN)));
        Assert.assertEquals(false, new User(id, username, Role.ADMIN).equals(new User(id, username, role)));
        Assert.assertEquals(false, new User(id, differentUsername, role).equals(new User(id, username, role)));
        Assert.assertEquals(false, new User(differentId, username, role).equals(new User(id, username, role)));
        Assert.assertEquals(false, new User(id, username, role).equals(null));
    }
}
