package ru.mediatel.chatserver.dbdto.exception;

public class UnauthorizedException extends DomainException {
    public UnauthorizedException() {
        super(ResponseStatus.NOT_AUTHENTICATED, "Unauthorized");
    }
}
