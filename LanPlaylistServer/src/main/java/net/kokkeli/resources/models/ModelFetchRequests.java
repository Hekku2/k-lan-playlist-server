package net.kokkeli.resources.models;

import java.util.ArrayList;

/**
 * Model for fetch request list
 * @author Hekku2
 */
public class ModelFetchRequests extends ViewModel{
    private ArrayList<ModelFetchRequest> items = new ArrayList<ModelFetchRequest>();
    
    /**
     * Fetch requests
     * @return fetch requests
     */
    @ModelCollection
    public ArrayList<ModelFetchRequest> getItems(){
        return items;
    }
}
