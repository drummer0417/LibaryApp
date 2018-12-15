package nl.androidappfactory.libraryapp.category;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import nl.androidappfactory.libraryapp.common.json.JsonReader;
import nl.androidappfactory.libraryapp.model.Category;

import javax.enterprise.context.ApplicationScoped;
import java.util.List;

@ApplicationScoped
public class CategoryJsonConverter {

    public Category convertFrom(String json) {

        JsonObject jsonObject = JsonReader.readAsJsonObject(json);
        Category category = new Category();

        category.setName(JsonReader.getStringOrNull(jsonObject, "name"));
        return category;
    }

    public JsonElement convertToJsonElement(final Category category) {
        final JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("id", category.getId());
        jsonObject.addProperty("name", category.getName());
        return jsonObject;
    }

    public JsonElement convertToJsonElement(final List<Category> categories) {
        final JsonArray jsonArray = new JsonArray();

        for (final Category category : categories) {
            jsonArray.add(convertToJsonElement(category));
        }

        return jsonArray;
    }

}
