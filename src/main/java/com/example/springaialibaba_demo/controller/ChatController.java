package com.example.springaialibaba_demo.controller;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.chat.memory.InMemoryChatMemory;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

import java.util.List;

/**
 * @description: 普通对话
 * @author: mayalong
 * @create: 2025-09-16 11:05
 **/
@RestController
@RequestMapping("/ai")
public class ChatController {

    private final ChatClient chatClient;


    //    public ChatController(ChatClient.Builder chatClient) {
//        this.chatClient = chatClient
//                .build();
//    }
    public ChatController(ChatClient.Builder chatClient) {
        this.chatClient = chatClient
                .defaultSystem("你是一个演员,请列出你所参演的所有电源")//初始化构建角色
                .build();
    }


    @GetMapping("/chat")
    public String chat(@RequestParam("input") String input) {
        return this.chatClient.prompt().user(input).call().content();
    }

    @GetMapping(value = "/stream", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<String> streamChat(@RequestParam("input") String input) {
        return this.chatClient.prompt().user(input).stream().content();
    }

    /**
     * 创建一个数据结构
     *
     * @param actor              作者
     * @param movies
     * @param recommendedWebpage
     */
    record ActorFilm(String actor, List<String> movies, List<String> recommendedWebpage) {
    }


    /**
     * 指定输出josn格式ActorFilm
     *
     * @param input
     * @return
     */
    @GetMapping("/movies")
    public ActorFilm movies(@RequestParam("input") String input) {
        return this.chatClient.prompt().user(input).call().entity(ActorFilm.class);
    }


}
