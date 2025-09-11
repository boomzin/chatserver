package ru.mediatel.chatserver.dbdto.response;

public class StatusApiResponse implements ApiResponse {
    private final int status;
    private final boolean isSuccess;

    public StatusApiResponse(int status, boolean isSuccess) {
        this.status = status;
        this.isSuccess = isSuccess;
    }

    @Override
    public int getStatus() {
        return status;
    }

    @Override
    public boolean isSuccess() {
        return isSuccess;
    }
}
