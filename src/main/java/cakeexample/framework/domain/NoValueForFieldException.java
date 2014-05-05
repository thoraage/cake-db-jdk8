package cakeexample.framework.domain;

public class NoValueForFieldException extends RuntimeException {
    public final Field<?, ?> field;

    public NoValueForFieldException(String message, Field<?, ?> field) {
        super(message);
        this.field = field;
    }
}
