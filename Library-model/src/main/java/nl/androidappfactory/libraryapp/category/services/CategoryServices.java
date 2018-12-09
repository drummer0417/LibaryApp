package nl.androidappfactory.libraryapp.category.services;

import nl.androidappfactory.libraryapp.model.Category;

import java.util.List;

public interface CategoryServices {

    public Category add(Category category);

    public Category update(Category category);

    public Category findById(Long l);

    List<Category> findAll(String name);
}
