package nl.androidappfactory.libraryapp.exceptions;

public class FieldNotValidException extends RuntimeException {

    private final static long serialVersionUID = 8021209912641056082L;

    private String field;

    public FieldNotValidException(String field, String message) {
        super(message);
        this.field = field;
    }

    @Override
    public String toString() {
        return "FieldNotValidException{" +
                "field='" + field + '\'' +
                '}';
    }

    public String getField() {
        return field;
    }
}
