package com.vinod.ptcp_app.controller;

import com.vinod.ptcp_app.entity.Message;
import com.vinod.ptcp_app.service.ChatService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/chat")
@RequiredArgsConstructor
public class ChatRestController {

    private final ChatService chatService;

    @GetMapping("/{senderId}/{receiverId}")
    public ResponseEntity<List<Message>> getChatHistory(@PathVariable Long senderId, @PathVariable Long receiverId) {
        return new ResponseEntity<>(chatService.getChatHistory(senderId, receiverId), HttpStatus.OK);
    }
}

