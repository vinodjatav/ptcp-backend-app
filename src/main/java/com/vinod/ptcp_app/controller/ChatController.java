package com.vinod.ptcp_app.controller;

import com.vinod.ptcp_app.entity.Message;
import com.vinod.ptcp_app.service.ChatService;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

@Controller
@RequiredArgsConstructor
public class ChatController {

    private final ChatService chatService;

    @MessageMapping("/chat") // Clients send messages to "/app/chat"
    @SendTo("/topic/messages") // Broadcast messages to "/topic/messages"
    public Message handleChatMessage(@Payload Message message) {
        return chatService.saveMessage(message);
    }
}

