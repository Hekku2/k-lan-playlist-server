package net.kokkeli.ripservice;

import java.io.File;

import net.kokkeli.ISettings;
import net.kokkeli.data.FetchRequest;
import net.kokkeli.data.ILogger;
import net.kokkeli.data.LogSeverity;

public class YouTubeRipper implements IFetcher{
    private final ISettings settings;
    private final ILogger logger;
    
    private final static String TYPE = "youtube";
    
    private final static String START_MINIMIZED = "--qt-start-minimized";
    
    public YouTubeRipper(ISettings setting, ILogger logger){
        this.settings = setting;
        this.logger = logger;
    }
    
    @Override
    public void fetch(FetchRequest request) throws FetchFailedException{
        try {
            String command = String.format("%s\\vlc.exe \"%s\" %s --sout=#transcode{acodec=vorb,channels=2}:standard{access=file,mux=ogg,dst=\"%s\"} vlc://quit",
                    settings.getVlcLocation(),
                    request.getLocation(),
                    START_MINIMIZED,
                    request.getDestinationFile());
            Process p = Runtime.getRuntime().exec(command);
            p.waitFor();
            
            File file = new File(request.getDestinationFile());
            if (file.length() == 0){
                boolean deleted = file.delete();
                throw new FetchFailedException(String.format("File was 0 bytes for some reason. File was %s deleted. File location: %s, source: %s", deleted ? "" : "not", request.getDestinationFile(), request.getLocation()));
            }
            //TODO Somehow removing item if it was not found. If not possible with vlc-command, just check for 0-bytes.
        }catch (FetchFailedException e){
            logger.log("Fetching failed: " + e.getMessage(), LogSeverity.ERROR);
            throw e;
        }catch (Exception e) {
            logger.log("Shit hit the fan. " + e.getMessage(), LogSeverity.ERROR);
            throw new FetchFailedException("Fetching failed: " + e.getMessage());
        }
    }

    @Override
    public String getHandledType() {
        return TYPE;
    }
}
