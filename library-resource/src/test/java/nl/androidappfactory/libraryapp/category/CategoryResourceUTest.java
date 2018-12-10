package nl.androidappfactory.libraryapp.category;

import nl.androidappfactory.libraryapp.category.resource.repository.CategoryRepository;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static nl.androidappfactory.libraryapp.commontests.category.CategoryForTestRepository.*;
import static org.mockito.Mockito.*;
import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;


import static org.junit.Assert.*;

public class CategoryResourceUTest {

    private CategoryResource categoryResource;

    @Mock
    private CategoryRepository categoryRepository;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);

        categoryResource = new CategoryResource();
    }

    @Test
    public void aTest(){


    }
}