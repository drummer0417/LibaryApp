package nl.androidappfactory.libraryapp.category.repository;

import nl.androidappfactory.libraryapp.model.Category;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.util.List;

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

    public List<Category> findAll(String orderField) {
        return (List<Category>) em.createQuery("SELECT e FROM Category e ORDER BY e." + orderField).getResultList();
    }

    public boolean alreadyExists(Category category) {

        StringBuilder jpql = new StringBuilder();
        jpql.append("SELECT 1 FROM Category e WHERE e.name = :name");
//        if (category.getId() != null) {
//            jpql.append(" And e.id != :id");
//        }

        Query query = em.createQuery(jpql.toString());
        query.setParameter("name", category.getName());
        if (category.getId() != null) {
            query.setParameter("id", category.getId());
        }

        return query.setMaxResults(1).getResultList().size() > 0;
    }

    public boolean existsByID(Long id) {
        if (id == null) {
            return false;
        }
        return em.createQuery("SELECT 1 FROM Category e WHERE e.id = :id ")
                .setMaxResults(1)
                .setParameter("id", id)
                .getResultList().size() > 0;
    }
}
