package net.albertogarrido.stepcounter.database;

import net.albertogarrido.stepcounter.panoramio.model.Panoramio;

import java.util.List;

public class DatabaseManager implements IDatabaseManager {
    @Override
    public void savePanorama(Panoramio panorama) {
        List<Panoramio> results = Panoramio.find(Panoramio.class, "photo_id = ?", panorama.getPhotoID().toString());
        if(results.size() == 0){
            panorama.save();
        }
    }

    @Override
    public List<Panoramio> getPanoramas() {
        List<Panoramio> results = Panoramio.find(Panoramio.class, null, null, null, "id DESC", null);
        return results;
    }

    @Override
    public void deleteAllPanoramas() {
        Panoramio.deleteAll(Panoramio.class);
    }
}
