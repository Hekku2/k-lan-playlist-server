package net.kokkeli.resources;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

import net.kokkeli.data.ILogger;
import net.kokkeli.data.Role;
import net.kokkeli.data.Session;
import net.kokkeli.data.User;
import net.kokkeli.data.services.ISessionService;
import net.kokkeli.player.IPlayer;
import net.kokkeli.server.ITemplateService;

import org.junit.Before;

public abstract class ResourceTestsBase {
    private ILogger mockLogger;
    private ITemplateService mockTemplateService;
    private IPlayer mockPlayer;
    private ISessionService mockSessionService;
    
    @Before
    public void setup() throws Exception{
        mockLogger = mock(ILogger.class);
        mockTemplateService = mock(ITemplateService.class);
        mockPlayer = mock(IPlayer.class);
        mockSessionService = mock(ISessionService.class);
        
        User loggerUser = new User("LoggedUser", Role.ADMIN);
        Session session = new Session(loggerUser);
        when(mockSessionService.get(any(String.class))).thenReturn(session);
        
        before();
    }
    
    public HttpServletRequest buildRequest(){
        HttpServletRequest req = mock(HttpServletRequest.class);
        when(req.getCookies()).thenReturn(buildAuthenticationCookies());
        
        return req;
        
    }
    
    private Cookie[] buildAuthenticationCookies(){
        Cookie[] cookies = new Cookie[1];
        cookies[0] = new Cookie("auth", "");
        
        return cookies;
    }
    
    public abstract void before() throws Exception;
    
    public ILogger getLogger(){
        return mockLogger;
    }
    
    public ITemplateService getTemplateService(){
        return mockTemplateService;
    }
    
    public IPlayer getPlayer(){
        return mockPlayer;
    }
    
    public ISessionService getSessionService(){
        return mockSessionService;
    }
}
