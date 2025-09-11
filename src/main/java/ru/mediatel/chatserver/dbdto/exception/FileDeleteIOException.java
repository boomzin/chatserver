package ru.mediatel.chatserver.dbdto.exception;


public class FileDeleteIOException extends RuntimeException {

    public FileDeleteIOException(Exception cause) {
        super(cause);
    }
}
