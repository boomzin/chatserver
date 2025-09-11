package ru.mediatel.chatserver.rest;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

@RestController
@RequestMapping("/chat")
public class ChatController {

    private final ConcurrentMap<String, ChatSession> sessions = new ConcurrentHashMap<>();

    @PostMapping("/start")
    public String startChat() throws JsonProcessingException {
        String chatId = UUID.randomUUID().toString();
        sessions.put(chatId, new ChatSession(chatId));
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
