package net.kokkeli.ripservice;

import java.io.File;

import net.kokkeli.data.dto.FetchRequest;
import net.kokkeli.data.dto.ILogger;
import net.kokkeli.data.dto.LogSeverity;
import net.kokkeli.data.dto.UploadType;
import net.kokkeli.settings.ISettings;

public class YoutubeDlRipper implements IFetcher {
    private final ISettings settings;
    private final ILogger logger;
    
    private final static UploadType TYPE = UploadType.YOUTUBEDL;
    
    public YoutubeDlRipper(ISettings setting, ILogger logger){
        this.settings = setting;
        this.logger = logger;
    }
    
    @Override
    public void fetch(FetchRequest request) throws FetchFailedException {
        try {
            String command = String.format("%s\\youtube-dl.exe \"%s\" -x --audio-format vorbis -o \"%s\"",
                    settings.getYoutubeDlLocation(),
                    request.getLocation(),
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
        return TYPE.getText();
    }

}
