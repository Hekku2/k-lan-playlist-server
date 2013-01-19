package net.kokkeli.resources.models;

import java.util.ArrayList;

import net.kokkeli.resources.ModelCollection;
import net.kokkeli.resources.Field;
import net.kokkeli.resources.Model;

/**
 * View model for playlist
 * @author Hekku2
 *
 */
@Model
public class ModelPlaylist extends ViewModel{
    private ArrayList<ModelPlaylistItem> items = new ArrayList<ModelPlaylistItem>();

    public ModelPlaylist(){
        for (int i = 0; i < 12; i++) {
            ModelPlaylistItem item = new ModelPlaylistItem();
            item.setTrackName("Song " + i);
            item.setArtist("Jarmo kostaaa" + i);
            
            items.add(item);
        }
    }
    
    @Field
    public String getName(){
        return "MockName";
    }
    
    @ModelCollection
    public ArrayList<ModelPlaylistItem> getItems(){
        return items;
    }
    
}
