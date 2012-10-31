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

import com.sun.jersey.api.NotFoundException;

/**
 * Purpose of this class is to load css-files from resources when they are requested from server.
 * 
 * @author Hekku2
 * @version 0.01
 */
@Path("/css/{file}")
public class CssResource {
    private static final String CSS_FOLDER = "target\\classes\\net\\kokkeli\\resources\\css\\";
    
    /**
     * Loads css-file with given name from resources.
     * @param file Name of file.
     * @return Css-file
     */
    @GET
    @Produces("text/html")
    public StreamingOutput getCSS(@PathParam("file") final String file) {
        
        return new StreamingOutput() {
            /**
             * Writes file to output-stream.
             * @param output Used outputstream
             * @exception<IOException> Thrown if there is unknown problem with input.
             * @exception<NotFoundException> Thrown if given file was not found
             */
            public void write(OutputStream output) throws IOException, NotFoundException {
                try {
                    FileInputStream fis = new FileInputStream(CSS_FOLDER + file);
                    
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
