package net.kokkeli.data.db;

import java.util.ArrayList;
import java.util.Collection;

import net.kokkeli.data.Track;

public class TrackDatabase implements ITrackDatabase{

    @Override
    public void CheckDatabaseFormat() throws DatabaseException {
    }

    @Override
    public Track get(long id) throws DatabaseException, NotFoundInDatabase {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Collection<Track> get() throws DatabaseException {
        //TODO Proper implementation
        ArrayList<Track> tracks = new ArrayList<Track>();
        for (int i = 0; i < 3; i++) {
            Track track = new Track();
            track.setArtist("Artist! " + i);
            track.setTrackName("TrackName" + i);
            track.setLocation("Location"+i);
            track.setId(i);
            tracks.add(track);
        }
        return tracks;
    }

    @Override
    public Track add(Track item) throws DatabaseException {
        return null;
    }

}
