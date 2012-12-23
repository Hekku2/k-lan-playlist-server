package net.kokkeli.server;

import static org.junit.Assert.*;

import java.io.IOException;

import junit.framework.Assert;

import net.kokkeli.ISettings;
import net.kokkeli.resources.Field;
import net.kokkeli.resources.models.ModelPlaylist;
import net.kokkeli.resources.models.ViewModel;

import org.junit.Before;
import org.junit.Test;

import freemarker.template.TemplateModelException;

import static org.mockito.Mockito.*;

public class TestTemplates {
    private static final String CORRECT_TEMPLATE_LOCATION = "target\\classes\\net\\kokkeli\\resources\\views";
    private static final String CORRECT_TEMPLATE = "index.ftl";
    
    private ITemplateService templateService;

    @Before
    public void setUp() throws Exception {
        ISettings mockSettings = mock(ISettings.class);
        when(mockSettings.getTemplatesLocation()).thenReturn(CORRECT_TEMPLATE_LOCATION);
        
        templateService = new Templates(mockSettings);
    }

    @Test
    public void testProcessingThrowsExceptionWithWrongParameters() throws IOException, TemplateModelException {
        try {
            templateService.process("");
            fail("Processing should have thrown a rendering exception.");
        } catch (RenderException e) {
            Assert.assertEquals("Template with name:  was not found", e.getMessage());
        }
        
        try {
            templateService.process(null);
            fail("Processing should have thrown a rendering exception.");
        } catch (RenderException e) {
            Assert.assertEquals("Template name cant be null.", e.getMessage());
        }
    }

    @Test
    public void testProcessingModelThrowsExceptionWithWrongParameters() throws IOException, TemplateModelException {
        try {
            templateService.process("", new ModelPlaylist());
            fail("Processing should have thrown a rendering exception.");
        } catch (RenderException e) {
            Assert.assertEquals("Template with name:  was not found", e.getMessage());
        }

        try {
            templateService.process(null, new ModelPlaylist());
            fail("Processing should have thrown a rendering exception.");
        } catch (RenderException e) {
            Assert.assertEquals("Template name cant be null.", e.getMessage());
        }
        
        try {
            templateService.process(CORRECT_TEMPLATE, null);
            fail("Processing should have thrown a rendering exception.");
        } catch (RenderException e) {
            Assert.assertEquals("Model can't be null.", e.getMessage());
        }
    }
    
    @Test
    public void testInitializingThrowsExceptionWithWrongParameters() throws TemplateModelException{
        final String nonExistingTemplate = "asdf";
        
        try {
            ISettings mockSettings = mock(ISettings.class);
            when(mockSettings.getTemplatesLocation()).thenReturn("");
            
            new Templates(mockSettings);
        } catch (IOException e) {
            Assert.assertEquals("", e.getMessage());
        }
        
        try {
            ISettings mockSettings = mock(ISettings.class);
            when(mockSettings.getTemplatesLocation()).thenReturn(nonExistingTemplate);
            
            new Templates(mockSettings);
        } catch (IOException e) {
            Assert.assertEquals(nonExistingTemplate +" does not exist.", e.getMessage());
        }
    }
    
    @Test
    public void testProcessingWithCorrectValuesDoesntThrowException() throws IOException, RenderException, TemplateModelException{
        /*
        String result = Templates.process(CORRECT_TEMPLATE);
        Assert.assertNotNull("Result should have value.",result);
        Assert.assertTrue("Result should have been longer.", result.length() > 1);
        */
        String result = templateService.process(CORRECT_TEMPLATE, new ModelPlaylist());
        Assert.assertNotNull("Result should have value.", result);
        Assert.assertTrue("Result should have been longer.", result.length() > 1);
    }
    
     @Test
     public void testProcessingThrowCorrectExceptionIfModelHasInvalidFields(){
         ViewModel invalidField = new ViewModel(){
             @Field
             public void getMake(String pekka){
                 return;
             }
         };
         
         try {
             templateService.process(CORRECT_TEMPLATE, invalidField);
            Assert.fail("Processing template with invalid model should throw exception.");
        } catch (Exception e) {
            Assert.assertEquals("Provided viewmodel contained Field-annotations with arguments.", e.getMessage());
        }
         
     }
}
