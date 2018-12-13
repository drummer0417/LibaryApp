package nl.androidappfactory.libraryapp.category.services;

import nl.androidappfactory.libraryapp.category.exceptions.CategoryExistsException;
import nl.androidappfactory.libraryapp.category.exceptions.CategoryNotFoundException;
import nl.androidappfactory.libraryapp.category.repository.CategoryRepository;
import nl.androidappfactory.libraryapp.exceptions.FieldNotValidException;
import nl.androidappfactory.libraryapp.model.Category;

import javax.validation.ConstraintViolation;
import javax.validation.Validator;

import java.util.Iterator;
import java.util.List;
import java.util.Set;

public class CategoryServicesImpl implements CategoryServices {

    Validator validator;
    CategoryRepository categoryRepository;

    @Override
    public Category add(Category category) {

        validate(category);
        if (categoryRepository.alreadyExists(category)) {
            throw new CategoryExistsException();
        }
        Category addedCategory = categoryRepository.add(category);
        return addedCategory;
    }

    @Override
    public Category update(Category category) {

        validate(category);

        if (categoryRepository.alreadyExists(category)) {
            throw new CategoryExistsException();
        }
        if (!categoryRepository.existsByID(category.getId())) {
            throw new CategoryNotFoundException();
        }
        categoryRepository.update(category);
        return null;
    }

    @Override
    public Category findById(Long id) {


        Category category = categoryRepository.findById(id);

        if (category == null) {
            throw new CategoryNotFoundException();
        }
        return category;
    }

    @Override
    public List<Category> findAll() {

        return categoryRepository.findAll("name");
    }

    private void validate(Category category) {
        Set<ConstraintViolation<Category>> errors = validator.validate(category);
        Iterator<ConstraintViolation<Category>> itErrors = errors.iterator();
        if (itErrors.hasNext()) {
            ConstraintViolation<Category> violation = itErrors.next();
            throw new FieldNotValidException(violation.getPropertyPath().toString(), violation.getMessage());
        }
    }
}
