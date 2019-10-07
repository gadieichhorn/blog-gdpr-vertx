//package com.rds.gdpr.patterns.controller;
//
//import com.rds.gdpr.patterns.service.ChatMessageProducer;
//import com.rds.gdpr.patterns.model.ChatMessage;
//import lombok.RequiredArgsConstructor;
//import org.reactivestreams.Publisher;
//import org.springframework.http.MediaType;
//import org.springframework.web.bind.annotation.*;
//import reactor.core.publisher.Flux;
//import reactor.core.publisher.Mono;
//
//@RestController
//@RequestMapping("/chat")
//@RequiredArgsConstructor
//public class ChatController {
//
//    private final ChatMessageProducer chatMessageProducer;
//
//    @GetMapping(value = "/sse/{id}", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
//    Publisher<ChatMessage> messages(@PathVariable String id) {
//        return chatMessageProducer.messages(id);
//    }
//
//    @GetMapping("/{id}")
//    private Mono<ChatMessage> getChatMessageById(@PathVariable String id) {
//        return employeeRepository.findEmployeeById(id);
//    }
//
//    @GetMapping
//    private Flux<ChatMessage> getAllChatMessages() {
//        return employeeRepository.findAllEmployees();
//    }
//
//    @PostMapping()
//    private Mono<ChatMessage> crateChatMessage(@RequestBody ChatMessage employee) {
//        return employeeRepository.updateEmployee(employee);
//    }
//}
