package ru.mediatel.chatserver.dbdto.tool;

public record ToolParam(
        String name,
        String description,
        String type,
        boolean required
) {}
