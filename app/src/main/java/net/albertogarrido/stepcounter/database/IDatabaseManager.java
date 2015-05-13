package net.albertogarrido.stepcounter.database;

import net.albertogarrido.stepcounter.panoramio.model.Panoramio;

import java.util.List;

public interface IDatabaseManager {

    void savePanorama(Panoramio panorama);

    List<Panoramio> getPanoramas();

    void deleteAllPanoramas();
}
