package nl.androidappfactory.libraryapp.common.json;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

public class JsonUtils {

    public static JsonElement getJsonElementWithId(final Long id) {
        final JsonObject idJson = new JsonObject();
        idJson.addProperty("id", id);

        return idJson;
    }
}
