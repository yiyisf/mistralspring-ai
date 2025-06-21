package com.example.mistralspringai.service;


import org.springframework.ai.chat.messages.Message;
import org.springframework.ai.chat.messages.MessageType;
import org.springframework.ai.chat.prompt.*;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class PromptService {

    public Message generatePromptByMessageType(MessageType messageType, String template, Map<String, Object> variables) {
        switch (messageType){
            case ASSISTANT, USER -> {
                return new AssistantPromptTemplate(template).createMessage(variables);
            }
            case SYSTEM -> {
                return new SystemPromptTemplate(template).createMessage(variables);
            }
            case TOOL -> {
                return new FunctionPromptTemplate(template).createMessage(variables);
            }
            default -> new AssistantPromptTemplate(template).createMessage(variables);
        }
        return null;
    }

}
