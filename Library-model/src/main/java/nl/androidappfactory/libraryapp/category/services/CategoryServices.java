package nl.androidappfactory.libraryapp.category.services;

import nl.androidappfactory.libraryapp.model.Category;

import javax.ejb.Local;
import java.util.List;

@Local
public interface CategoryServices {

    public Category add(Category category);

    public Category update(Category category);

    public Category findById(Long l);

    List<Category> findAll();
}
