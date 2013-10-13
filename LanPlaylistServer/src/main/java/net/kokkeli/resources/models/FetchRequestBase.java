package net.kokkeli.resources.models;

import net.kokkeli.resources.Field;

/**
 * Base model containing data all fetch request models use
 * @author Hekku2
 *
 */
public abstract class FetchRequestBase extends ViewModel{
    private String destination;
    private String location;
    private String handler;
    
    /**
     * Returns location of model
     * @return Model location
     */
    @Field
    public String getLocation() {
        return location;
    }

    /**
     * Sets location of model
     * @param location Location
     */
    public void setLocation(String location) {
        this.location = location;
    }
    
    /**
     * Sets handler of model
     * @param handler Handler
     */
    public void setHandler(String handler) {
        this.handler = handler;
    }
    
    /**
     * Returns handler of model
     * @return Model handler
     */
    @Field
    public String getHandler() {
        return handler;
    }
    
    /**
     * Gets destination
     * @return Destination
     */
    @Field
    public String getDestination() {
        return destination;
    }

    /**
     * Sets destiantion
     * @param destination Destination
     */
    public void setDestination(String destination) {
        this.destination = destination;
    }
}
