package nl.androidappfactory.libraryapp.commontests.category;

import nl.androidappfactory.libraryapp.model.Category;
import org.junit.Ignore;

import java.util.Arrays;
import java.util.List;

@Ignore
public class CategoryForTestRepository {

    public static Category java() {
        return new Category("Java");
    }

    public static Category cleanCode() {
        return new Category("Clean Code");
    }

    public static Category architecture() {
        return new Category("Architecture");
    }

    public static Category networks() {
        return new Category("Networks");
    }

    public static List<Category> allCategories() {
        return Arrays.asList(java(), cleanCode(), architecture(), networks());
    }

}
