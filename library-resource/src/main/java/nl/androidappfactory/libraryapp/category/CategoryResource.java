package nl.androidappfactory.libraryapp.category;

import nl.androidappfactory.libraryapp.category.exceptions.CategoryExistsException;
import nl.androidappfactory.libraryapp.category.services.CategoryServices;
import nl.androidappfactory.libraryapp.common.json.JsonUtils;
import nl.androidappfactory.libraryapp.common.json.OperationResultJsonWriter;
import nl.androidappfactory.libraryapp.common.model.HttpCode;
import nl.androidappfactory.libraryapp.common.model.OperationResult;
import nl.androidappfactory.libraryapp.model.Category;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.core.Response;

public class CategoryResource {

    private Logger logger = LoggerFactory.getLogger(getClass());
    private CategoryServices categoryServices;
    private CategoryJsonConverter categoryJsonConverter;

    public CategoryResource() {
        categoryJsonConverter = new CategoryJsonConverter();
    }

    public CategoryResource(CategoryServices categoryServices) {
        this();
        this.categoryServices = categoryServices;
    }

    public Response add(String categoryJson) {

        logger.debug("Adding new categoryJson: {}", categoryJson);
        Category category = categoryJsonConverter.convertFrom(categoryJson);

        try {

            category = categoryServices.add(category);

            OperationResult result = OperationResult.success(JsonUtils.getJsonElementWithId(category.getId()));

            logger.debug("OperationResult after addeing: {}", result);
            return Response.status(HttpCode.CREATED).entity(OperationResultJsonWriter.toJson(result)).build();

        } catch (CategoryExistsException e) {

            OperationResult result = OperationResult.error("category.existent", "There is already a category for the given name");

            logger.error("OperationResult after addeing: {}", result);
            return Response.status(HttpCode.VALIDATION_ERROR).entity(OperationResultJsonWriter.toJson(result)).build();
        }
    }
}
