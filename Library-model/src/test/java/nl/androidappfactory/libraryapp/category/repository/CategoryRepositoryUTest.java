package nl.androidappfactory.libraryapp.category.repository;

import nl.androidappfactory.commontests.utils.DBCommand;
import nl.androidappfactory.commontests.utils.DBCommandTransactionalExecutor;
import nl.androidappfactory.libraryapp.model.Category;
import org.hibernate.hql.internal.ast.tree.IsNotNullLogicOperatorNode;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

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
            categoryAddedId = dbCommandTransactionalExecutor.excecuteCommand(() -> categoryRepository.add(java()).getId());
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
    public void modifyCategory() {

        Long categoryAddedId = dbCommandTransactionalExecutor.excecuteCommand(() -> categoryRepository.add(java()).getId());

        Category addedCategory = categoryRepository.findById(categoryAddedId);
        assertThat(addedCategory.getName(), is(equalTo(java().getName())));

        addedCategory.setName(cleanCode().getName());

        dbCommandTransactionalExecutor.excecuteCommand(() -> {
            categoryRepository.update(addedCategory);
            return null;
        });

        Category categoryAfterUpdate = dbCommandTransactionalExecutor.excecuteCommand(() -> categoryRepository.findById(categoryAddedId));
        assertThat(categoryAfterUpdate.getName(), is(equalTo(cleanCode().getName())));
    }
}