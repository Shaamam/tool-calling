package io.shaama.todoapp.chat;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api/v1/simple-chat")
public class ChatBotController {

    private final ChatService chatService;

    @PostMapping("/ask")
    public ChatBotResponse askQuestion(@RequestBody ChatBotRequest chatBotRequest) {
        return chatService.askQuestion(chatBotRequest);
    }

}
