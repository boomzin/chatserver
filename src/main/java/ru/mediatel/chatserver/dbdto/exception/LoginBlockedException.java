package ru.mediatel.chatserver.dbdto.exception;


import static ru.mediatel.chatserver.dbdto.exception.ResponseStatus.LOGIC_ERROR;

public class LoginBlockedException extends DomainException {

    public LoginBlockedException() {
        super(LOGIC_ERROR, "LOGIN_BLOCKED");
    }
}
