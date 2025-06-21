package com.example.mistralspringai.controller;

import com.example.mistralspringai.model.ActorsFilms;
import com.example.mistralspringai.service.PromptService;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.messages.Message;
import org.springframework.ai.chat.messages.MessageType;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.mistralai.MistralAiChatModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

import java.util.Map;

@RestController
public class ChatController {

    private final MistralAiChatModel chatModel;
    private final PromptService promptService;

    @Autowired
    public ChatController(MistralAiChatModel chatModel, PromptService promptService) {
        this.chatModel = chatModel;
        this.promptService = promptService;
    }

    @GetMapping("/ai/generate")
    public Map<String,String> generate(@RequestParam(value = "message", defaultValue = "讲个笑话") String message) {
        return Map.of("generation", this.chatModel.call(message));
    }

    @GetMapping("/ai/generateStream")
    public Flux<ChatResponse> generateStream(@RequestParam(value = "message", defaultValue = "讲个笑话") String message) {
        var prompt = new Prompt(new UserMessage(message));
        return this.chatModel.stream(prompt);
    }

    @GetMapping("/ai/generate/prompt")
    public Map<String,String> generateWithPrompt(@RequestParam(value = "message", defaultValue = "讲个笑话") String message) {
        String systemText = """
                你是一位乐于助人的人工智能助手，可以帮助人们查找信息或讲笑话。
                你的名字是{name}
                你应该用你的名字并以{voice}的语气回复用户的请求。
                """;
        Message userMessage = new UserMessage(message);
        Message systemMessage = promptService.generatePromptByMessageType(MessageType.SYSTEM, systemText, Map.of("name", "yiyi", "voice", "稳重"));
        return Map.of("generation", this.chatModel.call(userMessage, systemMessage));
    }

    @GetMapping("/ai/generate/formatout")
    public ActorsFilms generateWithPromptFormatOutput(@RequestParam(value = "actor", defaultValue = "汤姆·汉克斯") String actor) {
        String systemText = """
                你是一位乐于助人的人工智能助手，可以帮助人们查找信息或讲笑话。
                你的名字是{name}
                你应该用你的名字并以{voice}的语气回复用户的请求。
                使用中文回答。
                """;

        return ChatClient.create(chatModel).prompt()
                .user(u -> u.text("为 {actor} 生成 5 部电影的作品目录。")
                        .param("actor", actor))
                .system(s ->s.text(systemText).params(Map.of("name", "yiyi", "voice", "稳重")))
                .call()
                .entity(ActorsFilms.class);
    }
}
