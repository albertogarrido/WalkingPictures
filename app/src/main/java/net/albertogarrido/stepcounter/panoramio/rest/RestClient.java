package net.albertogarrido.stepcounter.panoramio.rest;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import net.albertogarrido.stepcounter.panoramio.deserializer.PanoramioDeserializer;
import net.albertogarrido.stepcounter.panoramio.model.Panoramio;

import retrofit.RestAdapter;
import retrofit.converter.GsonConverter;

public class RestClient {

    private static final String API_URL = "http://www.panoramio.com";
    private PanoramioAPI panoramioAPI;

    public RestClient() {
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.registerTypeAdapter(Panoramio.class, new PanoramioDeserializer());

        Gson gson = gsonBuilder.create();

        RestAdapter restAdapter = new RestAdapter.Builder()
                .setLogLevel(RestAdapter.LogLevel.NONE)
                .setEndpoint(API_URL)
                .setConverter(new GsonConverter(gson))
                .build();

        panoramioAPI = restAdapter.create(PanoramioAPI.class);
    }

    public PanoramioAPI getSuggestionAPI() {
        return panoramioAPI;
    }

}
