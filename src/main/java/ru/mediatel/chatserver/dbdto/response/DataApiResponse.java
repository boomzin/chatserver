package ru.mediatel.chatserver.dbdto.response;


import ru.mediatel.chatserver.dbdto.exception.ResponseStatus;

public class DataApiResponse<T> extends SuccessStatusApiResponse {
    private T data;

    public DataApiResponse(T data) {
        super(ResponseStatus.SUCCESS);
        this.data = data;
    }

    public T getData() {
        return data;
    }
}
