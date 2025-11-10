package ru.mediatel.chatserver.rest;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import dev.langchain4j.agent.tool.ToolSpecification;
import dev.langchain4j.memory.chat.MessageWindowChatMemory;
import dev.langchain4j.model.chat.request.json.JsonObjectSchema;
import dev.langchain4j.model.openai.OpenAiChatModel;
import dev.langchain4j.service.AiServices;
import dev.langchain4j.service.tool.ToolExecutor;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpHeaders;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;
import ru.mediatel.chatserver.dbdto.tool.Tool;
import ru.mediatel.chatserver.dbdto.tool.ToolParam;
import ru.mediatel.chatserver.service.WineAssistant;

import java.net.URI;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


class ChatSession {

    private final String chatId;
    private final String dbServiceUrl;
    private WineAssistant assistant;

    private final RestTemplate restTemplate = new RestTemplateBuilder()
            .defaultHeader(HttpHeaders.ACCEPT_CHARSET, "UTF-8")
            .build();
    private final ObjectMapper mapper = new ObjectMapper();


    ChatSession(String chatId, String dbServiceUrl) throws JsonProcessingException {
        this.chatId = chatId;
        this.dbServiceUrl = dbServiceUrl;

        List<Tool> toolList = mapper.readValue(restTemplate
                        .getForObject(this.dbServiceUrl + "/api/rest/db_service/v1/tools", String.class),
                new TypeReference<List<Tool>>() {}
        );

        Map<ToolSpecification, ToolExecutor> specificationExecutorMap = new HashMap<>();

        toolList.forEach(tool -> {
            ToolSpecification.Builder toolSpecificationBuilder = ToolSpecification.builder()
                    .name(tool.getDefinition().name())
                    .description(tool.getDefinition().description());

            JsonObjectSchema.Builder parametrBuilder = JsonObjectSchema.builder();
            List<String> requiredParams = new ArrayList<>();

            for (ToolParam toolParam : tool.getDefinition().params()) {
                if (toolParam.type().equalsIgnoreCase("string")) {
                    parametrBuilder.addStringProperty(toolParam.name(), toolParam.description());
                } else if (toolParam.type().equalsIgnoreCase("int")) {
                    parametrBuilder.addIntegerProperty(toolParam.name(), toolParam.description());
                }
                if (toolParam.required()) {
                    requiredParams.add(toolParam.name());
                }
            }

            ToolExecutor toolExecutor;

            if (tool.getDefinition().httpMethod().equalsIgnoreCase("GET")) {
                toolExecutor = (toolExecutionRequest, memoryId) -> {
                    UriComponentsBuilder builder = UriComponentsBuilder
                            .fromUriString(this.dbServiceUrl + tool.getDefinition().endpoint());

                    Map<String, Object> arguments = null;
                    try {
                        arguments = mapper.readValue(toolExecutionRequest.arguments(), new TypeReference<>() {});
                    } catch (JsonProcessingException e) {
                        throw new RuntimeException(e);
                    }

                    arguments.forEach((k, v) -> {
                        if (v != null) builder.queryParam(k, v);
                    });

                    URI uri = builder.build().toUri();
                    return restTemplate.getForObject(uri, String.class);
                };
            } else if (tool.getDefinition().httpMethod().equalsIgnoreCase("POST")) {
                toolExecutor = (toolExecutionRequest, memoryId) -> {

                    Map<String, Object> arguments = null;
                    try {
                        arguments = mapper.readValue(toolExecutionRequest.arguments(), new TypeReference<>() {});
                    } catch (JsonProcessingException e) {
                        throw new RuntimeException(e);
                    }

                    return restTemplate.postForEntity(
                            this.dbServiceUrl + tool.getDefinition().endpoint(),
                            arguments,
                            String.class
                    ).getBody();
                };
            } else {
                throw new UnsupportedOperationException("HTTP метод не поддерживается: " + tool.getDefinition().httpMethod());
            }

            specificationExecutorMap.put(
                    toolSpecificationBuilder
                            .name(tool.getDefinition().name())
                            .description(tool.getDefinition().description())
                            .parameters(parametrBuilder.
                                    required(requiredParams)
                                    .build())
                            .build(),
                    toolExecutor
            );
        });


        var memory = MessageWindowChatMemory.withMaxMessages(20);

        this.assistant = AiServices.builder(WineAssistant.class)
                .chatModel(OpenAiChatModel.builder()
                        .apiKey("demo")
                        .baseUrl("http://localhost:9090/v1")
                        .modelName("gpt-4o-mini")
                        .logRequests(true)
                        .logResponses(true)
                        .build())
                .chatMemory(memory)
                .tools(specificationExecutorMap)
                .build();
    }

    String chat(String message) {
        return assistant.chat(message);
    }
}