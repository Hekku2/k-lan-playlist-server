package net.kokkeli.acceptance;

import java.io.IOException;

import net.kokkeli.ISettings;
import net.kokkeli.Settings;
import net.kokkeli.server.LanServer;
import net.kokkeli.server.ServerException;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.phantomjs.PhantomJSDriver;

public abstract class BaseAcceptanceTest {
    private static final String DEFAULT_SETTINGS = "settings/default.dat";

    protected static ISettings settings;
    private static LanServer server;
    
    protected WebDriver driver;
    
    @BeforeClass
    public static void fixtureSetup() throws IOException, ServerException{
        settings = new Settings();
        settings.loadSettings(DEFAULT_SETTINGS);
        server = new LanServer(settings);
        server.start();
    }
    
    @AfterClass
    public static void fixtureTeardown() throws Exception{
        server.stop();
    }
    
    @Before
    public void beforeTest(){
        driver = new PhantomJSDriver();
    }
    
    @After
    public void afterTest(){
        if (driver != null)
            driver.quit();
    }
}
