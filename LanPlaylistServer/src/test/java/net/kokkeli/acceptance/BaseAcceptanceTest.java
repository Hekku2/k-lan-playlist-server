package net.kokkeli.acceptance;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import net.kokkeli.ISettings;
import net.kokkeli.Settings;
import net.kokkeli.acceptance.pages.BasePage;
import net.kokkeli.acceptance.pages.PageAuthentication;
import net.kokkeli.server.LanServer;
import net.kokkeli.server.ServerException;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.phantomjs.PhantomJSDriver;

public abstract class BaseAcceptanceTest {
    private static final String DEFAULT_SETTINGS = "settings/acceptanceTests.dat";

    protected static final String ADMIN_USERNAME = "admin";
    protected static final String USER_USERNAME = "user";
    protected static final String DEFAULT_PASSWORD = "kokkeli";
    
    protected static final long TEST_PLAYLIST_ID = 1;
    
    protected static ISettings settings;
    private static LanServer server;
    private static Connection connection;
    
    protected WebDriver driver;
    
    @BeforeClass
    public static void fixtureSetup() throws IOException, ServerException, ClassNotFoundException, SQLException{
        settings = new Settings();
        settings.loadSettings(DEFAULT_SETTINGS);
        Class.forName("org.sqlite.JDBC");
        connection = DriverManager.getConnection("jdbc:sqlite:" + settings.getDatabaseLocation());
        
        try (Reader reader = new FileReader("db/test_data.sql")){
            ScriptRunner runner = new ScriptRunner(connection, false, true);
            runner.runScript(reader);
        }
        server = new LanServer(settings);
        server.start();
    }
    
    @AfterClass
    public static void fixtureTeardown() throws Exception{
        server.stop();
    }
    
    @Before
    public void beforeTest() throws FileNotFoundException, IOException, SQLException{
        try (Reader reader = new FileReader("db/test_data.sql")){
            ScriptRunner runner = new ScriptRunner(connection, false, true);
            runner.runScript(reader);
        }
        driver = new PhantomJSDriver();
    }
    
    @After
    public void afterTest(){
        if (driver != null)
            driver.quit();
    }
    
    protected BasePage authenticate(String username, String password){
        PageAuthentication page = new PageAuthentication(settings, driver);
        page.open();
        page.LogIn(username, password);
        return page;
    }
    
    protected void logOut(){
        driver.findElement(By.cssSelector("a[href*='authentication']")).click();
        
    }
}
