package com.example.springaialibaba_demo.controller;

import jakarta.servlet.http.HttpServletResponse;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.chat.memory.InMemoryChatMemory;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

import java.util.List;

import static org.springframework.ai.chat.client.advisor.AbstractChatMemoryAdvisor.CHAT_MEMORY_CONVERSATION_ID_KEY;
import static org.springframework.ai.chat.client.advisor.AbstractChatMemoryAdvisor.CHAT_MEMORY_RETRIEVE_SIZE_KEY;

/**
 * @description: 基于内存的对话记忆
 * @author: mayalong
 * @create: 2025-09-16 11:05
 **/
@RestController
@RequestMapping("/chat-memory")
public class ChatMemoryController {

    private final ChatClient chatClient;

    //初始化基于内存的对话记忆
    ChatMemory chatMemory = new InMemoryChatMemory();
    public ChatMemoryController(ChatModel chatModel) {

        this.chatClient = ChatClient
                .builder(chatModel)
                .defaultSystem("你是一个旅游规划师，请根据用户的需求提供旅游规划建议。")
                .defaultAdvisors(new MessageChatMemoryAdvisor(new InMemoryChatMemory()))
                .build();
    }

    @GetMapping("/in-memory")
    public Flux<String> memory(
            @RequestParam("prompt") String prompt,
            @RequestParam("chatId") String chatId,
            HttpServletResponse response
    ) {

        response.setCharacterEncoding("UTF-8");

        return chatClient.prompt(prompt).advisors(
                a -> a
                        .param(CHAT_MEMORY_CONVERSATION_ID_KEY, chatId)
                        .param(CHAT_MEMORY_RETRIEVE_SIZE_KEY, 100)//100轮对话
        ).stream().content();
    }


}
