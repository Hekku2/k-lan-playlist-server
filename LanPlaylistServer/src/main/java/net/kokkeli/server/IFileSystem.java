package net.kokkeli.server;

import java.io.IOException;
import java.io.InputStream;

/**
 * Interface for fileoperations
 * @author Hekku2
 *
 */
public interface IFileSystem {
    
    /**
     * Writes uploaded file to disk.
     * @param uploadedInputStream Inputstream
     * @param uploadedFileLocation Location of file
     * @throws IOException thrown if there is problem with uploading.
     */
    void writeToFile(InputStream uploadedInputStream, String uploadedFileLocation) throws IOException;
    
    /**
     * Checks if file exists.
     * @param file File name
     * @return True, if file exists.
     */
    boolean fileExists(String file);
}
