package nl.androidappfactory.libraryapp.category.repository;

import nl.androidappfactory.commontests.utils.DBCommandTransactionalExecutor;
import nl.androidappfactory.libraryapp.model.Category;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import java.util.List;

import static nl.androidappfactory.libraryapp.commontests.category.CategoryForTestRepository.*;
import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;

public class CategoryRepositoryUTest {

    private EntityManagerFactory emf;
    private EntityManager em;
    private CategoryRepository categoryRepository;
    private DBCommandTransactionalExecutor dbCommandTransactionalExecutor;

    @Before
    public void initTestCase() {
        emf = Persistence.createEntityManagerFactory("libraryPU");
        em = emf.createEntityManager();

        categoryRepository = new CategoryRepository(em);
        dbCommandTransactionalExecutor = new DBCommandTransactionalExecutor(em);
    }

    @After
    public void closeEntityManager() {

        em.close();
        emf.close();
    }

    @Test
    public void addCategoryAndFindIt() {

        Long categoryAddedId = null;
        try {
            categoryAddedId = dbCommandTransactionalExecutor.executeCommand(() -> categoryRepository.add(java()).getId());
            assertThat(categoryAddedId, is(notNullValue()));
        } catch (Exception e) {
            fail("This exception should not have been thrown...");
        }
        Category category = categoryRepository.findById(categoryAddedId);
        assertThat(category, is(notNullValue()));
        assertThat(category.getName(), is(equalTo(java().getName())));
    }

    @Test
    public void findByIdNotFound() {

        Category category = categoryRepository.findById(999L);
        assertThat(category, is(nullValue()));
    }

    @Test
    public void findByIdNull() {

        Category category = categoryRepository.findById(null);
        assertThat(category, is(nullValue()));
    }

    @Test
    public void updateCategory() {

        Long categoryAddedId = dbCommandTransactionalExecutor.executeCommand(() -> categoryRepository.add(java()).getId());

        Category addedCategory = categoryRepository.findById(categoryAddedId);
        assertThat(addedCategory.getName(), is(equalTo(java().getName())));

        addedCategory.setName(cleanCode().getName());

        dbCommandTransactionalExecutor.executeCommand(() -> {
            categoryRepository.update(addedCategory);
            return null;
        });

        Category categoryAfterUpdate = categoryRepository.findById(categoryAddedId);
        assertThat(categoryAfterUpdate.getName(), is(equalTo(cleanCode().getName())));
    }

    @Test
    public void findAllCategories() {

        List<Category> categories = allCategories();


        dbCommandTransactionalExecutor.executeCommand(() -> {
            categories.forEach(categoryRepository::add);
            return null;
        });

        List<Category> categoriesFound = categoryRepository.findAll("name");
        assertEquals(4, categoriesFound.size());
        assertThat(categoriesFound.get(0).getName(), is(equalTo(architecture().getName())));
        assertThat(categoriesFound.get(1).getName(), is(equalTo(cleanCode().getName())));
        assertThat(categoriesFound.get(2).getName(), is(equalTo(java().getName())));
        assertThat(categoriesFound.get(3).getName(), is(equalTo(networks().getName())));
    }

    @Test
    public void alreadyExists() {

        dbCommandTransactionalExecutor.executeCommand(() -> categoryRepository.add(java()));

        boolean exists = categoryRepository.alreadyExists(java());
        assertThat(exists, is(true));
        assertThat(categoryRepository.alreadyExists(java()), is(equalTo(true)));
        assertThat(categoryRepository.alreadyExists(cleanCode()), is(equalTo(false)));
    }

//    @Test
//    public void alreadyExistsCategoryWithId() {
//        Category java = dbCommandTransactionalExecutor.executeCommand(() -> {
//            categoryRepository.add(cleanCode());
//            return categoryRepository.add(java());
//        });
//
//        assertThat(categoryRepository.alreadyExists(java), is(equalTo(false)));
//
//        java.setName(cleanCode().getName());
//        assertThat(categoryRepository.alreadyExists(java), is(equalTo(true)));
//
//        java.setName(networks().getName());
//        assertThat(categoryRepository.alreadyExists(java), is(equalTo(false)));
//    }

    @Test
    public void existById() {

        Category category = dbCommandTransactionalExecutor.executeCommand(() -> categoryRepository.add(java()));

        assertThat(categoryRepository.existsByID(category.getId()), is(equalTo(true)));
        assertThat(categoryRepository.existsByID(123L), is(equalTo(false)));

    }
}

