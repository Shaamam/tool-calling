package io.shaama.todoapp.config;

import io.shaama.todoapp.todo.TodoTools;
import lombok.RequiredArgsConstructor;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.vertexai.gemini.VertexAiGeminiChatModel;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class ChatToolConfig {

    private final VertexAiGeminiChatModel vertexAiGeminiChatModel;

    private final TodoTools todoTools;

    @Bean
    public ChatClient chatClient() {
        return ChatClient.builder(vertexAiGeminiChatModel)
                .defaultTools(todoTools)
                .build();
    }
}
