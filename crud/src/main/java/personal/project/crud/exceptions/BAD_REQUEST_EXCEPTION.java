package personal.project.crud.exceptions;

public class BAD_REQUEST_EXCEPTION extends RuntimeException{

    public BAD_REQUEST_EXCEPTION() {
        super();
    }

    public BAD_REQUEST_EXCEPTION(String message) {
        super(message);
    }

    public BAD_REQUEST_EXCEPTION(String message, Throwable cause) {
        super(message, cause);
    }

    public BAD_REQUEST_EXCEPTION(Throwable cause) {
        super(cause);
    }

    protected BAD_REQUEST_EXCEPTION(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
