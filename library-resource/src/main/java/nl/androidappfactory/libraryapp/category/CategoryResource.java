package nl.androidappfactory.libraryapp.category;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import nl.androidappfactory.libraryapp.category.exceptions.CategoryExistsException;
import nl.androidappfactory.libraryapp.category.exceptions.CategoryNotFoundException;
import nl.androidappfactory.libraryapp.category.services.CategoryServices;
import nl.androidappfactory.libraryapp.common.json.JsonUtils;
import nl.androidappfactory.libraryapp.common.json.JsonWriter;
import nl.androidappfactory.libraryapp.common.json.OperationResultJsonWriter;
import nl.androidappfactory.libraryapp.common.model.HttpCode;
import nl.androidappfactory.libraryapp.common.model.OperationResult;
import nl.androidappfactory.libraryapp.common.model.ResourceMessage;
import nl.androidappfactory.libraryapp.exceptions.FieldNotValidException;
import nl.androidappfactory.libraryapp.model.Category;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static nl.androidappfactory.libraryapp.common.model.StandardsOperationResults.*;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;

@Path("/categories")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class CategoryResource {

    private static final ResourceMessage RESOURCE_MESSAGE = new ResourceMessage("category");

    private Logger logger = LoggerFactory.getLogger(getClass());

    @Inject
    private CategoryServices categoryServices;

    @Inject
    private CategoryJsonConverter categoryJsonConverter;

    public CategoryResource() {
        categoryJsonConverter = new CategoryJsonConverter();
    }

    public CategoryResource(CategoryServices categoryServices) {
        this();
        this.categoryServices = categoryServices;
    }

    @POST
    public Response add(String body) {

        logger.debug("Adding new categoryJson: {}", body);
        Category category = categoryJsonConverter.convertFrom(body);

        HttpCode httpCode = HttpCode.CREATED;
        OperationResult result = null;
        try {
            category = categoryServices.add(category);
            result = OperationResult.success(JsonUtils.getJsonElementWithId(category.getId()));
        } catch (final FieldNotValidException e) {
            logger.error("One of the fields of the category is not valid", e);
            httpCode = HttpCode.VALIDATION_ERROR;
            result = getOperationResultInvalidField(RESOURCE_MESSAGE, e);
        } catch (final CategoryExistsException e) {
            logger.error("There's already a category for the given name", e);
            httpCode = HttpCode.VALIDATION_ERROR;
            result = getOperationResultExistent(RESOURCE_MESSAGE, "name");
        }

        logger.debug("Returning the operation result after adding category: {}", result);
        return Response.status(httpCode.getCode()).entity(OperationResultJsonWriter.toJson(result)).build();
    }

    @PUT
    @Path("/{id}")
    public Response update(@PathParam("id") final Long id, final String body) {
        logger.debug("Updating the category {} with body {}", id, body);
        final Category category = categoryJsonConverter.convertFrom(body);
        category.setId(id);

        HttpCode httpCode = HttpCode.OK;
        OperationResult result;
        try {
            categoryServices.update(category);
            result = OperationResult.success();
        } catch (final FieldNotValidException e) {
            logger.error("One of the field of the category is not valid", e);
            httpCode = HttpCode.VALIDATION_ERROR;
            result = getOperationResultInvalidField(RESOURCE_MESSAGE, e);
        } catch (final CategoryExistsException e) {
            logger.error("There is already a category for the given name", e);
            httpCode = HttpCode.VALIDATION_ERROR;
            result = getOperationResultExistent(RESOURCE_MESSAGE, "name");
        } catch (final CategoryNotFoundException e) {
            logger.error("No category found for the given id", e);
            httpCode = HttpCode.NOT_FOUND;
            result = getOperationResultNotFound(RESOURCE_MESSAGE);
        }

        logger.debug("Returning the operation result after updating category: {}", result);
        return Response.status(httpCode.getCode()).entity(OperationResultJsonWriter.toJson(result)).build();
    }

    @GET
    @Path("{id}")
    public Response findById(@PathParam("id") final Long id) {
        logger.debug("Find category: {}", id);
        Response.ResponseBuilder responseBuilder;
        try {
            final Category category = categoryServices.findById(id);
            final OperationResult result = OperationResult
                    .success(categoryJsonConverter.convertToJsonElement(category));
            responseBuilder = Response.status(HttpCode.OK.getCode()).entity(OperationResultJsonWriter.toJson(result));
            logger.debug("Category found: {}", category);
        } catch (final CategoryNotFoundException e) {
            logger.error("No category found for id", id);
            responseBuilder = Response.status(HttpCode.NOT_FOUND.getCode());
        }

        return responseBuilder.build();
    }

    @GET
    public Response findAll() {
        logger.debug("Find all categories");

        final List<Category> categories = categoryServices.findAll();

        logger.debug("Found {} categories", categories.size());

        final JsonElement jsonWithPagingAndEntries = getJsonElementWithPagingAndEntries(categories);

        return Response.status(HttpCode.OK.getCode()).entity(JsonWriter.writeToString(jsonWithPagingAndEntries))
                .build();
    }

    private JsonElement getJsonElementWithPagingAndEntries(final List<Category> categories) {
        final JsonObject jsonWithEntriesAndPaging = new JsonObject();

        final JsonObject jsonPaging = new JsonObject();
        jsonPaging.addProperty("totalRecords", categories.size());

        jsonWithEntriesAndPaging.add("paging", jsonPaging);
        jsonWithEntriesAndPaging.add("entries", categoryJsonConverter.convertToJsonElement(categories));

        return jsonWithEntriesAndPaging;
    }

}