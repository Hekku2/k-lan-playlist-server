package net.kokkeli.ripservice;

import net.kokkeli.ISettings;
import net.kokkeli.data.FetchRequest;
import net.kokkeli.data.ILogger;
import net.kokkeli.data.LogSeverity;

public class YouTubeRipper implements IFetcher{
    private final ISettings settings;
    private final ILogger logger;
    
    private final static String TYPE = "youtube";
    
    public YouTubeRipper(ISettings setting, ILogger logger){
        this.settings = setting;
        this.logger = logger;
    }
    
    public void fetch(FetchRequest request){
        try {
            String command = String.format("%s\\vlc.exe %s --sout=#transcode{acodec=ogg,channels=2}:standard{access=file,mux=raw,dst=%s\\%s} vlc://quit",
                    settings.getVlcLocation(),
                    request.getLocation(),
                    settings.getTracksFolder(),
                    request.getDestinationFile());
            Process p = Runtime.getRuntime().exec(command);
            p.waitFor();
        }catch (Exception e) {
            logger.log("Shit hit the fan. " + e.getMessage(), LogSeverity.ERROR);
            //TODO Error handling
        }
    }

    @Override
    public String getHandledType() {
        return TYPE;
    }
}
