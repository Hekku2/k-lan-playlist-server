package net.kokkeli.server;

import static org.junit.Assert.*;

import java.io.IOException;

import junit.framework.Assert;

import net.kokkeli.resources.models.ModelPlaylist;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import freemarker.template.TemplateModelException;

public class TestTemplates {
    private static final String CORRECT_TEMPLATE_LOCATION = "target\\classes\\net\\kokkeli\\resources\\views";
    private static final String CORRECT_TEMPLATE = "index.ftl";
    
    @BeforeClass
    public static void setUpBeforeClass() throws Exception {
    }

    @AfterClass
    public static void tearDownAfterClass() throws Exception {
    }

    @Before
    public void setUp() throws Exception {
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void testProcessingThrowsExceptionWithWrongParameters() throws IOException, TemplateModelException {
        Templates.initialize(CORRECT_TEMPLATE_LOCATION);
        
        try {
            Templates.process("");
            fail("Processing should have thrown a rendering exception.");
        } catch (RenderException e) {
            Assert.assertEquals("Template with name:  was not found", e.getMessage());
        }
        
        try {
            Templates.process(null);
            fail("Processing should have thrown a rendering exception.");
        } catch (RenderException e) {
            Assert.assertEquals("Template name cant be null.", e.getMessage());
        }
    }

    @Test
    public void testProcessingModelThrowsExceptionWithWrongParameters() throws IOException, TemplateModelException {
        Templates.initialize(CORRECT_TEMPLATE_LOCATION);
        
        try {
            Templates.process("", new ModelPlaylist());
            fail("Processing should have thrown a rendering exception.");
        } catch (RenderException e) {
            Assert.assertEquals("Template with name:  was not found", e.getMessage());
        }

        try {
            Templates.process(null, new ModelPlaylist());
            fail("Processing should have thrown a rendering exception.");
        } catch (RenderException e) {
            Assert.assertEquals("Template name cant be null.", e.getMessage());
        }
        
        try {
            Templates.process(CORRECT_TEMPLATE, null);
            fail("Processing should have thrown a rendering exception.");
        } catch (RenderException e) {
            Assert.assertEquals("Model can't be null.", e.getMessage());
        }
    }
    
    @Test
    public void testInitializingThrowsExceptionWithWrongParameters() throws TemplateModelException{
        try {
            Templates.initialize("");
        } catch (IOException e) {
            Assert.assertEquals("", e.getMessage());
        }
        
        try {
            Templates.initialize("asdfd");
        } catch (IOException e) {
            Assert.assertEquals("asdfd does not exist.", e.getMessage());
        }
    }
    
    @Test
    public void testProcessingWithCorrectValuesDoesntThrowException() throws IOException, RenderException, TemplateModelException{
        Templates.initialize(CORRECT_TEMPLATE_LOCATION);
        
        String result = Templates.process(CORRECT_TEMPLATE);
        Assert.assertNotNull("Result should have value.",result);
        Assert.assertTrue("Result should have been longer.", result.length() > 1);
        
        result = Templates.process(CORRECT_TEMPLATE, new ModelPlaylist());
        Assert.assertNotNull("Result should have value.", result);
        Assert.assertTrue("Result should have been longer.", result.length() > 1);
    }
}
