package net.albertogarrido.stepcounter.views;

import net.albertogarrido.stepcounter.panoramio.model.Panoramio;

import java.util.List;

/**
 * Created by AlbertoGarrido on 31/3/15.
 */
public interface WalkActivityView extends WalkView {
    void registerBroadcast();

    void setPanoramasAdapter(List<Panoramio> panoramas);
}
