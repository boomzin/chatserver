package ru.mediatel.chatserver.rest;

import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.mediatel.chatserver.config.Config;

import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

@Slf4j
@RestController
@RequestMapping("/chat")
public class ChatController {

    private final ConcurrentMap<String, ChatSession> sessions = new ConcurrentHashMap<>();

    private final Config config;

    public ChatController(Config config) {
        this.config = config;
        log.info("start with config: {}", config);
    }

    @PostMapping("/start")
    public String startChat() throws JsonProcessingException {
        String chatId = UUID.randomUUID().toString();
        sessions.put(chatId, new ChatSession(chatId, config.getDbServiceUrl()));
        return chatId;
    }

    @PostMapping("/message")
    public String sendMessage(@RequestBody ChatRequest request) {
        ChatSession session = sessions.get(request.chatId());
        if (session == null) {
            throw new IllegalArgumentException("Unknown chatId: " + request.chatId());
        }
        return session.chat(request.message());
    }
}
