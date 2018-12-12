package nl.androidappfactory.libraryapp.common.json;

import com.google.gson.Gson;
import nl.androidappfactory.libraryapp.common.model.OperationResult;

public class OperationResultJsonWriter {

    private OperationResultJsonWriter() {
    }

    public static String toJson(final OperationResult operationResult) {
        if (operationResult.getEntity() == null) {
            return "";
        }

        final Gson gson = new Gson();
        return gson.toJson(operationResult.getEntity());
    }

}
