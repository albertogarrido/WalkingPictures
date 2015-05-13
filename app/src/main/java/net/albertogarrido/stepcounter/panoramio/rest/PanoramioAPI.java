package net.albertogarrido.stepcounter.panoramio.rest;

import net.albertogarrido.stepcounter.panoramio.model.Panoramio;

import retrofit.http.GET;
import retrofit.http.Query;

public interface PanoramioAPI {

    /*
    * Get the latest picture on panoramio API
    * @todo add maxx and maxy to the search
    * */
    @GET("/map/get_panoramas.php?set=public&from=0&to=1&size=original&mapfilter=true")
    public Panoramio getPanoramas(
                                    @Query("minx") Double minx,
                                    @Query("miny") Double miny
                                  );
}
