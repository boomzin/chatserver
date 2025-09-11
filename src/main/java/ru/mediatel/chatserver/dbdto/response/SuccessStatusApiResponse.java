package ru.mediatel.chatserver.dbdto.response;


import ru.mediatel.chatserver.dbdto.exception.ResponseStatus;

public class SuccessStatusApiResponse extends StatusApiResponse {
    public static SuccessStatusApiResponse SUCCESS = new SuccessStatusApiResponse(ResponseStatus.SUCCESS);

    public SuccessStatusApiResponse(ResponseStatus status) {
        super(status.getValue(), true);
    }
}
