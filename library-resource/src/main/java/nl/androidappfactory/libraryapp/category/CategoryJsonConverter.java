package nl.androidappfactory.libraryapp.category;

import com.google.gson.JsonObject;
import nl.androidappfactory.libraryapp.common.json.JsonReader;
import nl.androidappfactory.libraryapp.model.Category;

public class CategoryJsonConverter {

    public Category convertFrom(String json){

            JsonObject jsonObject = JsonReader.readAsJsonObject(json);
            Category category = new Category();

            category.setName(JsonReader.getStringOrNull(jsonObject, "name"));
            return category;
    }
}
