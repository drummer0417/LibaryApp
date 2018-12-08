package nl.androidappfactory.libraryapp.category.services;

import static org.mockito.Mock.*;

import static nl.androidappfactory.libraryapp.commontests.category.CategoryForTestRepository.*;
import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;

import nl.androidappfactory.libraryapp.category.repository.CategoryRepository;
import nl.androidappfactory.libraryapp.exceptions.FieldNotValidException;
import nl.androidappfactory.libraryapp.model.Category;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import javax.validation.Validation;
import javax.validation.Validator;

import static org.junit.Assert.*;

public class CategoryServicesUTest {

    CategoryServices categoryServices;
    CategoryRepository categoryRepository;
    Validator validator;

    @Before
    public void init() {

        categoryServices = new CategoryServicesImpl();
        validator = Validation.buildDefaultValidatorFactory().getValidator();
        ((CategoryServicesImpl) categoryServices).validator = validator;
    }

    @Test
    public void addCategoryWithNullNamme() {

        try {
            categoryServices.add(new Category());
            fail("An error should have been thrown");
        } catch (FieldNotValidException e) {
            assertThat(e.getField(), is(equalTo("name")));
            assertThat(e.getMessage(), is(equalTo("may not be null")));
        }
    }
}