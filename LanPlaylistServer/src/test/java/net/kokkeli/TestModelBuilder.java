package net.kokkeli;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import javax.ws.rs.core.MultivaluedMap;

import net.kokkeli.data.Role;
import net.kokkeli.data.services.ServiceException;
import net.kokkeli.resources.models.ModelPlaylistItem;
import net.kokkeli.resources.models.ModelUser;

import org.junit.Assert;
import org.junit.Test;

public class TestModelBuilder {
    
    @SuppressWarnings({ "unchecked", "static-method" })
    @Test
    public void testLongValueIsParsedCorrectly() throws ServiceException{
        ModelBuilder<ModelPlaylistItem> builder = new ModelBuilder<ModelPlaylistItem>(ModelPlaylistItem.class);
        
        MultivaluedMap<String, String> map = mock(MultivaluedMap.class);
        
        when(map.containsKey("id")).thenReturn(true);
        when(map.getFirst("id")).thenReturn("6  ");
        
        ModelPlaylistItem createdItem = builder.createModelFrom(map);
        Assert.assertEquals(6, createdItem.getId());
    }
    
    @SuppressWarnings({ "unchecked", "static-method" })
    @Test
    public void testStringValueIsUsedCorrectly() throws ServiceException  {
        ModelBuilder<ModelPlaylistItem> builder = new ModelBuilder<ModelPlaylistItem>(ModelPlaylistItem.class);
        
        MultivaluedMap<String, String> map = mock(MultivaluedMap.class);
        
        when(map.containsKey("artist")).thenReturn(true);
        when(map.getFirst("artist")).thenReturn("artist");
        
        ModelPlaylistItem createdItem = builder.createModelFrom(map);
        Assert.assertEquals("artist", createdItem.getArtist());
    }
    
    @SuppressWarnings({ "unchecked", "static-method" })
    @Test
    public void testBooleanValueIsUsedCorrectly() throws ServiceException  {
        ModelBuilder<ModelPlaylistItem> builder = new ModelBuilder<ModelPlaylistItem>(ModelPlaylistItem.class);
        
        MultivaluedMap<String, String> map = mock(MultivaluedMap.class);
        
        when(map.containsKey("exists")).thenReturn(true);
        when(map.getFirst("exists")).thenReturn("true");
        
        ModelPlaylistItem createdItem = builder.createModelFrom(map);
        Assert.assertEquals(true, createdItem.getExists());
        
        when(map.containsKey("exists")).thenReturn(true);
        when(map.getFirst("exists")).thenReturn("false");
        
        createdItem = builder.createModelFrom(map);
        Assert.assertEquals(false, createdItem.getExists());
    }
    
    @SuppressWarnings({ "unchecked", "static-method" })
    @Test
    public void testUserRoleValueIsParsedCorrectly() throws ServiceException{
        ModelBuilder<ModelUser> builder = new ModelBuilder<ModelUser>(ModelUser.class);
        
        MultivaluedMap<String, String> map = mock(MultivaluedMap.class);
        
        when(map.containsKey("role")).thenReturn(true);
        when(map.getFirst("role")).thenReturn("2");
        
        ModelUser createdItem = builder.createModelFrom(map);
        Assert.assertEquals(Role.USER, createdItem.getRoleEnum());
    }
}
