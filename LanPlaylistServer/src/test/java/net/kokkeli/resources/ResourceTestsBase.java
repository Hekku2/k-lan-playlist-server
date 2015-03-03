package net.kokkeli.resources;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;

import static org.junit.Assert.assertEquals;
import net.kokkeli.data.dto.ILogger;
import net.kokkeli.data.dto.Role;
import net.kokkeli.data.dto.Session;
import net.kokkeli.data.dto.User;
import net.kokkeli.data.services.ISessionService;
import net.kokkeli.player.IPlayer;
import net.kokkeli.player.PlayerStatus;
import net.kokkeli.resources.models.BaseModel;
import net.kokkeli.server.ITemplateService;
import net.kokkeli.settings.ISettings;

import org.junit.Assert;
import org.junit.Before;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

public abstract class ResourceTestsBase<T extends BaseResource> {
    //General response codes
    protected static final int RESPONSE_OK = 200;
    protected static final int REDIRECT = 303;
    protected static final int NOT_FOUND = 404;
    protected static final int INTERNAL_SERVER_ERROR = 500;
    protected static final int BAD_REQUEST = 400;
    
    //General form fields
    private static final String FORM_ID = "id";
    
    protected T resource;
    
    private ILogger mockLogger;
    private ITemplateService mockTemplateService;
    private IPlayer mockPlayer;
    private ISessionService mockSessionService;
    private ISettings mockSettings;
    
    @Before
    public void setup() throws Exception{
        mockLogger = mock(ILogger.class);
        mockTemplateService = mock(ITemplateService.class);
        mockPlayer = mock(IPlayer.class);
        mockSessionService = mock(ISessionService.class);
        mockSettings = mock(ISettings.class);
        
        User loggerUser = new User("LoggedUser", Role.ADMIN);
        Session session = new Session(loggerUser);
        when(mockSessionService.get(any(String.class))).thenReturn(session);
        
        when(mockPlayer.status()).thenReturn(new PlayerStatus());
        before();
        if (resource == null)
            Assert.fail("Resource under test was not initialized.");
    }
    
    public static HttpServletRequest buildRequest(){
        HttpServletRequest req = mock(HttpServletRequest.class);
        when(req.getCookies()).thenReturn(buildAuthenticationCookies());
        
        return req;
    }
    
    public void assertSessionError(String error){
        verify(getSessionService(), times(1)).setError(null, error);
    }
    
    public void assertSessionInfo(String info){
        verify(getSessionService(), times(1)).setInfo(null, info);
    }
    
    /**
     * Asserts that model contains correct response and modelanswer contains correct error and info.
     * @param r Response
     * @param model Model
     * @param error Error-text
     * @param info Info-text
     */
    public static void assertModelResponse(Response r, ModelAnswer model, String error, String info){
        assertModelResponse(RESPONSE_OK, r, model, error, info);
    }
    
    /**
     * Asserts that model contains correct response and modelanswer contains correct error and info.
     * @param r
     * @param model
     * @param error
     * @param info
     */
    public static void assertModelResponse(int statusCode, Response r, ModelAnswer model, String error, String info){
        assertEquals(error, model.getModel().getError());
        assertEquals(info, model.getModel().getInfo());
        assertEquals(statusCode, r.getStatus());
    }
    
    /**
     * Asserts that Response contains redirect and error
     * @param response
     * @param error
     */
    public void assertRedirectError(Response response, String error){
        assertEquals(REDIRECT, response.getStatus());
        assertSessionError(error);
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
    
    public ISettings getSettings(){
        return mockSettings;
    }
    
    public ISessionService getSessionService(){
        return mockSessionService;
    }
    
    private static Cookie[] buildAuthenticationCookies(){
        Cookie[] cookies = new Cookie[1];
        cookies[0] = new Cookie("auth", "");
        
        return cookies;
    }
    
    /**
     * Mocks MultivaluedMap for post containin id
     * @param id Id of post
     * @return Map containing id
     */
    protected static MultivaluedMap<String, String> createIdPost(String id){
        @SuppressWarnings("unchecked")
        MultivaluedMap<String, String> map = mock(MultivaluedMap.class);
        
        when(map.containsKey(FORM_ID)).thenReturn(true);
        
        when(map.getFirst(FORM_ID)).thenReturn(id);
        return map;
    }
    
    /**
     * Mocks MultivaluedMap for post containin id
     * @param id Id of post
     * @return Map containing id
     */
    protected static MultivaluedMap<String, String> createIdPost(long id){
        return createIdPost(id + "");
    }
    
    /**
     * Answer that holds Model
     * @author Hekku2
     *
     */
    protected static final class ModelAnswer implements Answer<String>{
        private BaseModel model;
        
        @Override
        public String answer(InvocationOnMock invocation) throws Throwable {
            model = (BaseModel)invocation.getArguments()[1];
            return "";
        }
        
        public BaseModel getModel(){
            return model;
        }
    }
}
