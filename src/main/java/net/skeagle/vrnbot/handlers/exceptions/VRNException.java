package net.skeagle.vrnbot.handlers.exceptions;

public class VRNException extends RuntimeException {
    private static final long serialVersionUID = 1L;
    private final String message;

    public VRNException(String message) {
        super("");
        this.message = message;
    }

    public String getMessage() {
        return this.message;
    }
}
