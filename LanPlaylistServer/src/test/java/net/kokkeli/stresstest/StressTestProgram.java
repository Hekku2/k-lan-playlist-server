package net.kokkeli.stresstest;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import net.kokkeli.server.LanServer;
import net.kokkeli.server.ServerException;

/**
 * This is a fast and dirty "stress test" to test that program can handle multiple connections.
 * @author Hekku2
 *
 */
public class StressTestProgram {
    private static final int USERCOUNT = 50;
    
    public static void main(String[] args) throws InterruptedException{
        TestServer server = null;
        ArrayList<Thread> threads = new ArrayList<Thread>();
        ArrayList<TestUser> users = new ArrayList<TestUser>();
        try {
            server = new TestServer();
            Thread serverThread = new Thread(server);
            threads.add(serverThread);
            serverThread.start();
            
            //Wait for server ready.
            Thread.sleep(2000);

            for (int i = 0; i < USERCOUNT; i++) {
                TestUser user = new TestUser();
                Thread th = new Thread(user);
                th.start();
                users.add(user);
                threads.add(th);
            }
            
            Thread.sleep(6000);
            
            System.out.println("Stopping tests.");
          } catch (Exception e) {
     
            e.printStackTrace();
     
          } finally {
          
              for (TestUser testUser : users) {
                  testUser.terminate();
                  while(!testUser.ready()){
                      Thread.sleep(200);
                  }
              }
              
              if (server != null){
                  server.terminate();
                  Thread.sleep(1200);
              }
              
              for (Thread thread : threads) {
                  thread.join();
              }
          }
          System.out.println("Testing finished.");
    }
    
    private static class TestUser implements Runnable {
        private volatile boolean running = true;
        private boolean stopped = false;
        private long calls = 0;
        private long failed = 0;
        
        public boolean ready(){
            return stopped;
        }
        
        public void terminate() {
            running = false;
        }
        
        @SuppressWarnings("resource")
        @Override
        public void run() {
            CloseableHttpResponse response = null;
            CloseableHttpClient client = null;
            try {
                client = HttpClients.createDefault();
                
                //Log in
                String loginUrl = "http://localhost:9998/authentication";
                List<NameValuePair> urlParameters = new ArrayList<NameValuePair>();
                urlParameters.add(new BasicNameValuePair("user", "admin"));
                urlParameters.add(new BasicNameValuePair("pwd", "kokkeli"));
                
                HttpPost post = new HttpPost(loginUrl);
                post.setEntity(new UrlEncodedFormEntity(urlParameters));
                
                response = client.execute(post);
                
                //Start spamming log page
                while(running){
                    String logUrl = "http://localhost:9998/log";
                    HttpGet get = new HttpGet(logUrl);
                    calls++;
                    response = client.execute(get);
                    if (response.getStatusLine().getStatusCode() != 200){
                        System.out.println("Log response: " + response.getStatusLine().getStatusCode());
                        failed++;
                    }
                    response.close();
                    Thread.sleep(5);
                }
                System.out.println("Failed: " +  failed);
                System.out.println("All: " + calls);
            } catch (Exception e) {
                System.out.println(e);
            } finally {
                if (response != null){
                    try {
                        response.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                if (client != null){
                    try {
                        client.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                stopped = true;
            }
        }
        
        
    }
    
    private static class TestServer implements Runnable{
        private volatile boolean running = true;
        
        public void terminate() {
            running = false;
        }
        
        @Override
        public void run() {
            LanServer server = null;
            try {
                System.setProperty("SQLite.encoding", "utf-8");
                String settingsFile = "settings/default.dat";

                
                System.out.println("Starting server with settings: " + settingsFile);
                server = new LanServer(settingsFile);
                server.start();

                while(running){
                    Thread.sleep(500);
                }
                
            } catch (ServerException | InterruptedException e) {
                System.out.println(e.toString());
            } finally {
                if (server != null){
                    try {
                        server.stop();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }            
            }
        }
    }

}
