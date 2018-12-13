package nl.androidappfactory.libraryapp.category.services;

import static nl.androidappfactory.libraryapp.commontests.category.CategoryForTestRepository.*;

import static org.mockito.Mockito.*;
import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;

import nl.androidappfactory.libraryapp.category.exceptions.CategoryExistsException;
import nl.androidappfactory.libraryapp.category.exceptions.CategoryNotFoundException;
import nl.androidappfactory.libraryapp.category.repository.CategoryRepository;
import nl.androidappfactory.libraryapp.exceptions.FieldNotValidException;
import nl.androidappfactory.libraryapp.model.Category;
import org.junit.Before;
import org.junit.Test;

import javax.validation.Validation;
import javax.validation.Validator;
import java.util.ArrayList;
import java.util.List;


public class CategoryServicesUTest {

    CategoryServices categoryServices;
    CategoryRepository categoryRepository;
    Validator validator;

    @Before
    public void init() {

        categoryRepository = mock(CategoryRepository.class);
        validator = Validation.buildDefaultValidatorFactory().getValidator();
        categoryServices = new CategoryServicesImpl();
        ((CategoryServicesImpl) categoryServices).validator = validator;
        ((CategoryServicesImpl) categoryServices).categoryRepository = categoryRepository;
    }

    //
    // ----------------- add category tests ---------------------
    //

    @Test
    public void addCategoryWithNullNamme() {
        addCategoryWithInvalidName(null);
    }

    @Test
    public void addCategoryWithShortNamme() {
        addCategoryWithInvalidName("x");
    }

    @Test
    public void addCategoryWithLongNamme() {
        addCategoryWithInvalidName("This should throw an invalidFieldException as the name is too long...");
    }

    @Test(expected = CategoryExistsException.class)
    public void addCategoryAlreadyExists() {

        when(categoryRepository.alreadyExists(any())).thenReturn(true);
        categoryServices.add(java());
    }

    @Test
    public void addValidCategory() {

        Category java = java();

        when(categoryRepository.alreadyExists(any())).thenReturn(false);
        when(categoryRepository.add(java)).thenReturn(categoryWithId(java, 1L));

        Category addedCategory = categoryServices.add(java);
        assertThat(addedCategory.getName(), is(equalTo(java().getName())));
        assertThat(addedCategory.getId(), is(equalTo(1L)));
    }

    //
    // ----------------- update category tests ---------------------
    //

    @Test
    public void updateCategoryWithNullNamme() {
        updateCategoryWithInvalidName(null);
    }

    @Test
    public void updateCategoryWithShortNamme() {
        updateCategoryWithInvalidName("x");
    }

    @Test
    public void updateCategoryWithLongNamme() {
        updateCategoryWithInvalidName("This should throw an invalidFieldException as the name is too long...");
    }

    @Test(expected = CategoryExistsException.class)
    public void updateCategoryWithExistingName() {

        when(categoryRepository.alreadyExists(any())).thenReturn(true);
        categoryServices.update(java());
    }

    @Test(expected = CategoryNotFoundException.class)
    public void updateCategoryNotExists() {

        when(categoryRepository.alreadyExists(any())).thenReturn(false);
        when(categoryRepository.existsByID(any())).thenReturn(false);
        categoryServices.update(java());
    }

    @Test
    public void updateCategoryValidCategory() {

        Category categoryJava = categoryWithId(java(), 1L);

        when(categoryRepository.alreadyExists(any())).thenReturn(false);
        when(categoryRepository.existsByID(any())).thenReturn(true);

        categoryServices.update(categoryJava);
        verify(categoryRepository).update(categoryJava);
    }

    //
    // ----------------- find category tests ---------------------
    //

    @Test
    public void findCategoryById() {

        when(categoryRepository.findById(anyLong())).thenReturn(categoryWithId(java(), 1L));

        Category categoryFound = categoryServices.findById(1L);
        assertThat(categoryFound.getId(), is(equalTo(1L)));
        assertThat(categoryFound.getName(), is(equalTo(java().getName())));
    }

    @Test(expected = CategoryNotFoundException.class)
    public void findCategoryByIdNotFound() {

        when(categoryRepository.findById(anyLong())).thenReturn(null);
        Category categoryFound = categoryServices.findById(1L);
    }

    @Test
    public void findAllNoCategories() {

        when(categoryRepository.findAll("name")).thenReturn(new ArrayList<Category>());

        List<Category> categories = categoryServices.findAll();
        assertThat(categories.isEmpty(), is(true));
    }

    @Test
    public void findAll() {

        when(categoryRepository.findAll("name")).thenReturn(allCategories());
        List<Category> categories = categoryServices.findAll();
        assertThat(categories.size(), is(equalTo(4)));
    }

    //
    // -------------------------------- test helper methods -------------------
    //
    private void addCategoryWithInvalidName(String name) {

        try {
            categoryServices.add(new Category(name));
            fail("An error should have been thrown");
        } catch (FieldNotValidException e) {
            assertThat(e.getFieldName(), is(equalTo("name")));
        }
    }

    private void updateCategoryWithInvalidName(String name) {

        try {
            categoryServices.update(new Category(name));
            fail("An error should have been thrown");
        } catch (FieldNotValidException e) {
            assertThat(e.getFieldName(), is(equalTo("name")));
        }
    }
}