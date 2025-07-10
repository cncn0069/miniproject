package edu.pnu.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import edu.pnu.domain.chat.ChatRoom;
import edu.pnu.service.ChatService;
import lombok.RequiredArgsConstructor;

@RestController
public class ChatController {
	@Autowired
	private ChatService chatService;

    @GetMapping("/rooms")
    public ChatRoom createRoom(@RequestParam String roomName) {
        return chatService.createRoom(roomName);
    } 
}
