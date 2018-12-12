package nl.androidappfactory.libraryapp.category;

import nl.androidappfactory.libraryapp.category.exceptions.CategoryExistsException;
import nl.androidappfactory.libraryapp.category.repository.CategoryRepository;
import nl.androidappfactory.libraryapp.category.services.CategoryServices;
import nl.androidappfactory.libraryapp.common.model.HttpCode;
import nl.androidappfactory.libraryapp.model.Category;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import javax.ws.rs.core.Response;

import static nl.androidappfactory.libraryapp.commontests.category.CategoryForTestRepository.*;
import static nl.androidappfactory.libraryapp.commontests.utils.TestFileName.*;
import static nl.androidappfactory.libraryapp.commontests.utils.JsonTestUtils.*;

import static org.mockito.Mockito.*;
import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

public class CategoryResourceUTest {

    private final static String PATH_TO_RESOURCE = "categories";

    private CategoryResource categoryResource;

    @Mock
    private CategoryServices categoryServices;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);

        categoryResource = new CategoryResource(categoryServices);
    }

    @Test
    public void addValidCategory(){

        String expectedJson = "{\"id\": 1}";
        Category categoryJava = java();

        when(categoryServices.add(any())).thenReturn(categoryWithId(categoryJava, 1L));

        Response response = categoryResource.add(readJsonFile(getPathFileRequest(PATH_TO_RESOURCE, "newCategory.json")));
        assertThat(response.getStatus(), is(equalTo(HttpCode.CREATED)));
        assertJsonMatchesExpectedJson(response.getEntity().toString(), expectedJson);
    }

    @Test
    public void addCategoryAreadyExists(){

        Category categoryJava = java();

        when(categoryServices.add(any())).thenThrow(CategoryExistsException.class);

        Response response = categoryResource.add(readJsonFile(getPathFileRequest(PATH_TO_RESOURCE, "newCategory.json")));
        assertThat(response.getStatus(), is(equalTo(HttpCode.VALIDATION_ERROR)));
    }

}