package nl.androidappfactory.libraryapp.category.repository;

import nl.androidappfactory.libraryapp.model.Category;

import javax.persistence.EntityManager;

public class CategoryRepository {

    private EntityManager em;

    public CategoryRepository() {
    }

    public CategoryRepository(EntityManager em) {
        this.em = em;
    }

    public Category add(Category category) {
        em.persist(category);
        return category;
    }

    public Category findById(Long id) {

        return id == null ? null : em.find(Category.class, id);
    }

    public void update(Category addedCategory) {
        em.merge(addedCategory);
    }
}
