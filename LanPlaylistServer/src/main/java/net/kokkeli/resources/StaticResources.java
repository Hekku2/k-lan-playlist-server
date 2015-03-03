package net.kokkeli.resources;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStream;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.StreamingOutput;

import net.kokkeli.settings.ISettings;

import com.google.inject.Inject;
import com.sun.jersey.api.NotFoundException;

/**
 * Purpose of this class is to load static resources from filesystem.
 * 
 * @author Hekku2
 * @version 0.01
 */
@Path("/resource")
public class StaticResources {
    private final String CSS_FOLDER;
    private final String IMAGES_FOLDER;
    private final String JS_FOLDER;
    private final String FONTS_FOLDER;
    
    /**
     * Creates static resources.
     * @param settings Settings
     */
    @Inject
    public StaticResources(ISettings settings){
        CSS_FOLDER = settings.getResourcesFolder() + "css/";
        IMAGES_FOLDER = settings.getResourcesFolder() + "images/";
        JS_FOLDER = settings.getResourcesFolder() + "js/";
        FONTS_FOLDER = settings.getResourcesFolder() + "fonts/";
    }
    
    /**
     * Loads css-file with given name from resources.
     * @param file Name of file.
     * @return Css-file
     */
    @GET
    @Produces("text/css")
    @Path("/css/{file: .*}")
    public StreamingOutput getCSS(@PathParam("file") final String file) {
        return getStream(CSS_FOLDER, file);
    }
    
    /**
     * Loads css-file with given name from resources.
     * @param file Name of file.
     * @return Css-file
     */
    @GET
    @Produces("font/opentype")
    @Path("/css/fonts/{file: .*}")
    public StreamingOutput getFonts(@PathParam("file") final String file) {
        return getStream(FONTS_FOLDER, file);
    }
    
    /**
     * Loads image-file with given name from resources.
     * @param file Name of file.
     * @return Image-file
     */
    @GET
    @Produces("image/png")
    @Path("/images/{file: .*}")
    public StreamingOutput getImage(@PathParam("file") final String file) {
        return getStream(IMAGES_FOLDER, file);
    }
    
    /**
     * Loads js-file with given name from resources.
     * @param file Name of file.
     * @return Css-file
     */
    @GET
    @Produces("text/javascript")
    @Path("/js/{file: .*}")
    public StreamingOutput getJs(@PathParam("file") final String file) {
        return getStream(JS_FOLDER, file);
    }
    
    /**
     * Buid Streaming output for resources.
     * Streaming output throws NotFoundException if file is not found.
     * @param source Source of file
     * @param file File name
     * @return Streaming output
     */
    private static StreamingOutput getStream(final String source, final String file) {
        return new StreamingOutput() {
            /**
             * Writes file to output-stream.
             * @param output Used outputstream
             * @exception<IOException> Thrown if there is unknown problem with input.
             * @exception<NotFoundException> Thrown if given file was not found
             */
            @Override
            public void write(OutputStream output) throws IOException, NotFoundException {
                try {
                    FileInputStream fis = new FileInputStream(source + file);
                    
                    byte[] buffer = new byte[4096];
                    int len; 
                    
                    while ((len = fis.read(buffer)) != -1)  
                    {  
                        output.write(buffer, 0, len);  
                    }  
                    output.flush();  
                    fis.close();  
                    output.close();
                } catch (FileNotFoundException e) {
                    throw new NotFoundException("Resource: " + file + " was not found.");
                }
            }
        };
    }
}
