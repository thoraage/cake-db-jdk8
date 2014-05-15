package cakeexample.framework.domain;

public class NoValueForFieldException extends RuntimeException {
    public final AbstractField<?, ?> field;

    public NoValueForFieldException(String message, AbstractField<?, ?> field) {
        super(message);
        this.field = field;
    }
}
