package nl.androidappfactory.libraryapp.category.services;

import com.sun.org.apache.xerces.internal.impl.dtd.models.ContentModelValidator;
import nl.androidappfactory.libraryapp.category.repository.CategoryRepository;
import nl.androidappfactory.libraryapp.exceptions.FieldNotValidException;
import nl.androidappfactory.libraryapp.model.Category;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintViolation;
import javax.validation.Validator;

import java.util.Iterator;
import java.util.Set;

public class CategoryServicesImpl implements CategoryServices {

    Validator validator;
    CategoryRepository categoryRepository;

    @Override
    public Category add(Category category) {

        Set<ConstraintViolation<Category>> errors = validator.validate(category);
        Iterator<ConstraintViolation<Category>> itErrors = errors.iterator();
        if (itErrors.hasNext()) {
            ConstraintViolation<Category> violation = itErrors.next();
            throw new FieldNotValidException(violation.getPropertyPath().toString(), violation.getMessage());
        }
        return null; //categoryRepository.add(category);
    }
}
