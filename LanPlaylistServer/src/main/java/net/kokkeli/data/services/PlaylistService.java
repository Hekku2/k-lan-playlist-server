package net.kokkeli.data.services;

import java.util.ArrayList;
import java.util.Collection;

import net.kokkeli.data.PlayListItem;

/**
 * PlaylistService
 * @author Hekku2
 *
 */
public class PlaylistService implements IPlaylistService {

    /**
     * Return playlist with current id.
     */
    @Override
    public Collection<PlayListItem> getPlaylist(long currentPlaylist) {
        Collection<PlayListItem> items = new ArrayList<PlayListItem>();
        
        for (int i = 0; i < 27; i++) {
            PlayListItem item = new PlayListItem();
            item.setArtist("Jarmon humppapumppu");
            item.setTrackName("Baras biisi " + i);
            
            items.add(item);
        }
        return items;
    }

}
