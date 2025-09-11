package ru.mediatel.chatserver.service;

import dev.langchain4j.model.openai.OpenAiChatModel;
import dev.langchain4j.service.AiServices;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.mediatel.chatserver.tools.WineShopTools;

@Slf4j
@Service
public class ChatService {

    private final WineAssistant assistant;

    public ChatService() {
        OpenAiChatModel model = OpenAiChatModel.builder()
                .baseUrl("http://langchain4j.dev/demo/openai/v1")
                .apiKey("any-key")
                .modelName("gpt-4o-mini")
                .logRequests(true)
                .logResponses(true)
                .build();

        this.assistant = AiServices.builder(WineAssistant.class)
                .chatModel(model)
                .tools(new WineShopTools())
                .build();
    }

    public String process(String userMessage) {
        return assistant.chat(userMessage);
    }
}
