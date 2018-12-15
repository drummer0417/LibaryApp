package nl.androidappfactory.libraryapp.category;

import nl.androidappfactory.libraryapp.category.exceptions.CategoryExistsException;
import nl.androidappfactory.libraryapp.category.exceptions.CategoryNotFoundException;
import nl.androidappfactory.libraryapp.category.repository.CategoryRepository;
import nl.androidappfactory.libraryapp.category.services.CategoryServices;
import nl.androidappfactory.libraryapp.common.model.HttpCode;
import nl.androidappfactory.libraryapp.exceptions.FieldNotValidException;
import nl.androidappfactory.libraryapp.model.Category;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import javax.ws.rs.core.Response;

import java.util.ArrayList;
import java.util.Arrays;

import static nl.androidappfactory.libraryapp.commontests.category.CategoryForTestRepository.*;
import static nl.androidappfactory.libraryapp.commontests.utils.TestFileName.*;
import static nl.androidappfactory.libraryapp.commontests.utils.JsonTestUtils.*;

import static org.mockito.Mockito.*;
import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

public class CategoryResourceUTest {

    private final static String PATH_RESOURCE = "categories";

    private CategoryResource categoryResource;

    @Mock
    private CategoryServices categoryServices;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);

        categoryResource = new CategoryResource(categoryServices);
    }

    @Test
    public void addValidCategory() {
        when(categoryServices.add(any())).thenReturn(categoryWithId(java(), 1L));

        final Response response = categoryResource.add(readJsonFile(getPathFileRequest(BASE_JSON_DIR + PATH_RESOURCE,
                "newCategory.json")));
        assertThat(response.getStatus(), is(equalTo(HttpCode.CREATED.getCode())));
        assertJsonMatchesExpectedJson(response.getEntity().toString(), "{\"id\": 1}");
    }

    @Test
    public void addExistentCategory() {
        when(categoryServices.add(any())).thenThrow(new CategoryExistsException());

        final Response response = categoryResource.add(readJsonFile(getPathFileRequest(BASE_JSON_DIR + PATH_RESOURCE,
                "newCategory.json")));
        assertThat(response.getStatus(), is(equalTo(HttpCode.VALIDATION_ERROR.getCode())));
//        assertJsonResponseWithFile(response, "categoryAlreadyExists.json");
    }

    @Test
    public void addCategoryWithNullName() {
        when(categoryServices.add(any())).thenThrow(new FieldNotValidException("name", "may not be null"));

        final Response response = categoryResource.add(readJsonFile(getPathFileRequest(BASE_JSON_DIR + PATH_RESOURCE,
                "categoryWithNullName.json")));
        assertThat(response.getStatus(), is(equalTo(HttpCode.VALIDATION_ERROR.getCode())));
//        assertJsonResponseWithFile(response, "categoryErrorNullName.json");
    }

    @Test
    public void updateValidCategory() {
        final Response response = categoryResource.update(1L,
                readJsonFile(getPathFileRequest(BASE_JSON_DIR + PATH_RESOURCE, "category.json")));
        assertThat(response.getStatus(), is(equalTo(HttpCode.OK.getCode())));
        assertThat(response.getEntity().toString(), is(equalTo("")));

        verify(categoryServices).update(any());
    }

    @Test
    public void updateCategoryWithNameBelongingToOtherCategory() {
        doThrow(new CategoryExistsException()).when(categoryServices).update(any());

        final Response response = categoryResource.update(1L,
                readJsonFile(getPathFileRequest(BASE_JSON_DIR + PATH_RESOURCE, "category.json")));
        assertThat(response.getStatus(), is(equalTo(HttpCode.VALIDATION_ERROR.getCode())));
//        assertJsonResponseWithFile(response, "categoryAlreadyExists.json");
    }

    @Test
    public void updateCategoryWithNullName() {
        doThrow(new FieldNotValidException("name", "may not be null")).when(categoryServices).update(any());

        final Response response = categoryResource.update(1L,
                readJsonFile(getPathFileRequest(BASE_JSON_DIR + PATH_RESOURCE, "categoryWithNullName.json")));
        assertThat(response.getStatus(), is(equalTo(HttpCode.VALIDATION_ERROR.getCode())));
//        assertJsonResponseWithFile(response, "categoryErrorNullName.json");
    }

    @Test
    public void updateCategoryNotFound() {
        Category catJava = java();
//        doThrow(new CategoryNotFoundException()).when(categoryServices).update(any());
        when(categoryServices.update(any())).thenThrow(CategoryNotFoundException.class);

        final Response response = categoryResource.update(2L,
                readJsonFile(getPathFileRequest(BASE_JSON_DIR + PATH_RESOURCE, "category.json")));
        assertThat(response.getStatus(), is(equalTo(HttpCode.NOT_FOUND.getCode())));
//        assertJsonResponseWithFile(response, "categoryNotFound.json");
    }

    @Test
    public void findCategory() {
        when(categoryServices.findById(1L)).thenReturn(categoryWithId(java(), 1L));

        final Response response = categoryResource.findById(1L);
        assertThat(response.getStatus(), is(equalTo(HttpCode.OK.getCode())));
//        assertJsonResponseWithFile(response, "categoryFound.json");
    }

    @Test
    public void findCategoryNotFound() {
        when(categoryServices.findById(1L)).thenThrow(new CategoryNotFoundException());

        final Response response = categoryResource.findById(1L);
        assertThat(response.getStatus(), is(equalTo(HttpCode.NOT_FOUND.getCode())));
    }

    @Test
    public void findAllNoCategory() {
        when(categoryServices.findAll()).thenReturn(new ArrayList<>());

        final Response response = categoryResource.findAll();
        assertThat(response.getStatus(), is(equalTo(HttpCode.OK.getCode())));
//        assertJsonResponseWithFile(response, "emptyListOfCategories.json");
    }

    @Test
    public void findAllTwoCategories() {
        when(categoryServices.findAll()).thenReturn(
                Arrays.asList(categoryWithId(java(), 1L), categoryWithId(networks(), 2L)));

        final Response response = categoryResource.findAll();
        assertThat(response.getStatus(), is(equalTo(HttpCode.OK.getCode())));
//        assertJsonResponseWithFile(response, "twoCategories.json");
    }


    private void assertJsonResponseWithFile(final Response response, final String fileName) {
        assertJsonMatchesExpectedJson(response.getEntity().toString(), getPathFileResponse(PATH_RESOURCE, fileName));
    }

}