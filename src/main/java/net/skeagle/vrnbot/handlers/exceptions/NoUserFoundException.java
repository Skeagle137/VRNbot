package net.skeagle.vrnbot.handlers.exceptions;

public class NoUserFoundException extends VRNException {
    private static final long serialVersionUID = 1L;

    public NoUserFoundException() {
        super("You must provide a valid id or mention of a user.");
    }

    public String getMessage() {
        return super.getMessage();
    }
}