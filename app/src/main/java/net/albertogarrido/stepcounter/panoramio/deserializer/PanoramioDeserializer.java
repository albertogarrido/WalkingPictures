package net.albertogarrido.stepcounter.panoramio.deserializer;

import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;

import net.albertogarrido.stepcounter.panoramio.model.Panoramio;

import java.lang.reflect.Type;

public class PanoramioDeserializer  implements JsonDeserializer<Panoramio> {

    public Panoramio deserialize(JsonElement json, Type type, JsonDeserializationContext deserializeContext)
            throws JsonParseException {

        Panoramio panoramio = new Panoramio();

        JsonObject result = json.getAsJsonObject();

        JsonArray photos = result.get("photos").getAsJsonArray();

        if(photos.size() > 0){
            for(int i = 0; i < photos.size(); i++){
                JsonObject photo = photos.get(i).getAsJsonObject();
                panoramio.setPhotoID(photo.get("photo_id").getAsInt());
                panoramio.setPhotoTitle(photo.get("photo_title").getAsString());
                panoramio.setPhotoFileUrl(photo.get("photo_file_url").getAsString());
            }
        }
        return panoramio;
    }
}
