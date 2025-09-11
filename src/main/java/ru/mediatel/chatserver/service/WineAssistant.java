package ru.mediatel.chatserver.service;

import dev.langchain4j.service.SystemMessage;
import dev.langchain4j.service.UserMessage;

public interface WineAssistant {

    @SystemMessage("Ты — помощник в винном магазине. Отвечай кратко и понятно. Информацию о списке вин ищи в инструментах")
    String chat(@UserMessage String message);
}
