package net.kokkeli.ripservice;

import static org.mockito.Mockito.mock;
import net.kokkeli.data.FetchRequest;
import net.kokkeli.data.FetchStatus;
import net.kokkeli.data.ILogger;
import net.kokkeli.data.LogSeverity;
import net.kokkeli.data.db.DatabaseException;
import net.kokkeli.data.db.IFetchRequestDatabase;
import net.kokkeli.data.db.NotFoundInDatabase;
import net.kokkeli.data.services.ServiceException;

import org.junit.Before;
import org.junit.Test;

import static org.mockito.Mockito.*;

public class TestFetcherRunner {
    private IFetchRequestDatabase mockDatabase;
    private IFetcher mockFetcher;
    private ILogger mockLogger;
    
    private String mockType = "mocktype";
    
    private FetcherRunner fetcherRunner;
    
    @Before
    public void setup() throws NotFoundInDatabase, DatabaseException, ServiceException{
        mockDatabase = mock(IFetchRequestDatabase.class);
        mockLogger = mock(ILogger.class);
        mockFetcher = mock(IFetcher.class);
        
        when(mockFetcher.getHandledType()).thenReturn(mockType);
        
        fetcherRunner = new FetcherRunner(mockLogger, mockFetcher, mockDatabase);
    }
    
    @Test
    public void testRunCallsCorrectServices() throws DatabaseException, InterruptedException, FetchFailedException{
        FetchRequest request = new FetchRequest();
        request.setId(666);
        
        when(mockDatabase.oldestUnhandledFetchRequestOrNull(mockType)).thenReturn(request);
        
        Thread t = new Thread(fetcherRunner);
        t.start();
        Thread.sleep(500);
        t.interrupt();
        
        verify(mockDatabase).updateRequest(request.getId(), FetchStatus.HANDLING);
        verify(mockFetcher).fetch(request);
        verify(mockDatabase).updateRequest(request.getId(), FetchStatus.HANDLED);
    }
    
    @Test
    public void testRunHandlesFetchRequestException() throws InterruptedException, DatabaseException, FetchFailedException{
        FetchRequest request = new FetchRequest();
        request.setId(666);
        
        when(mockDatabase.oldestUnhandledFetchRequestOrNull(mockType)).thenReturn(request);
        doThrow(new FetchFailedException("Fail")).when(mockFetcher).fetch(request);
        
        Thread t = new Thread(fetcherRunner);
        t.start();
        Thread.sleep(500);
        t.interrupt();
        verify(mockDatabase).updateRequest(request.getId(), FetchStatus.HANDLING);
        verify(mockFetcher).fetch(request);
        verify(mockDatabase).updateRequest(request.getId(), FetchStatus.ERROR);
    }
    
    @Test
    public void testRunHandlesDatabaseException() throws DatabaseException, InterruptedException{
        when(mockDatabase.oldestUnhandledFetchRequestOrNull(mockType)).thenThrow(new DatabaseException("Kaboom!"));
        
        Thread t = new Thread(fetcherRunner);
        t.start();
        Thread.sleep(500);
        t.interrupt();
        
        verify(mockLogger).log("Something went wrong with the database while processing fetch items. Kaboom!", LogSeverity.ERROR);
    }
}
