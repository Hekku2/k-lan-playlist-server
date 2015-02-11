package net.kokkeli.player;

public class PlayerServiceRunner {
    private static final String START_COMMAND = "start";
    
    private static PlayerServiceRunner service = new PlayerServiceRunner();
    private boolean stopped = false;
    
    public static void main(String[] args) {
        String cmd = START_COMMAND;
        if(args.length > 0) {
           cmd = args[0];
        }
      
        if("start".equals(cmd)) {
            service.start();
        }
        else {
            service.stop();
        }
    }

    /**
     * Start this service instance
     */
    public void start() {
       stopped = false;
       System.out.println("My Service Started " + new java.util.Date());
         
       while(!stopped) {
          System.out.println("My Service Executing " + new java.util.Date());
          synchronized(this) {
             try {
                this.wait(60000);  // wait 1 minute
             }
             catch(InterruptedException ie){
                 System.out.println("Interrupted.");
             }
          }
       }
         
       System.out.println("My Service Finished " + new java.util.Date());
    }
     
    /**
     * Stop this service instance
     */
    public void stop() {
       stopped = true;
       synchronized(this) {
          this.notify();
       }
    }
}
