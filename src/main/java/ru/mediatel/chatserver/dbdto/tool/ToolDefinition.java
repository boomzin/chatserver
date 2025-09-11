package ru.mediatel.chatserver.dbdto.tool;

import java.util.List;

public record ToolDefinition(
        String name,
        String description,
        String endpoint,
        String httpMethod,
        List<ToolParam> params
) {}