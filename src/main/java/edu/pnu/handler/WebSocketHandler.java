package edu.pnu.handler;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import com.fasterxml.jackson.databind.ObjectMapper;

import edu.pnu.domain.chat.ChatMessage;
import edu.pnu.domain.chat.ChatRoom;
import edu.pnu.domain.chat.MessageType;
import edu.pnu.service.ChatService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class WebSocketHandler extends TextWebSocketHandler {

    private final ObjectMapper objectMapper;
    private final ChatService chatService;
    private final Map<String, WebSocketSession> sessions = new ConcurrentHashMap<>();

    /**
     * 웹소켓 연결
     */
    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        log.info("[연결 완료] " + session.getId());
        var sessionId = session.getId();
        sessions.put(sessionId, session); // 세션에 저장
    }

    /**
     * 양방향 데이터 통신
     */
    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage textMessage) throws Exception {
        ChatMessage chatMessage = objectMapper.readValue(textMessage.getPayload(), ChatMessage.class);
        ChatRoom chatRoom = chatService.findRoomById(chatMessage.getRoomId());
        
        if(chatRoom != null) {
        	System.out.println(chatMessage.getType());
            if (chatMessage.getType().equals(MessageType.ENTER)) {
                chatRoom.addSession(session);
                chatMessage.setEnterMessage();
            }
            chatRoom.getSessions().parallelStream().forEach(s -> {
                if(!s.getId().equals(session.getId())) {
                    chatService.sendMessage(s, chatMessage);
                }
            });
        }
    }

    /**
     * 소켓 연결 종료
     */
    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        log.info("[연결 완료] " + session.getId());
        var sessionId = session.getId();
        sessions.remove(sessionId);
    }

    /**
     * 소켓 통신 에러
     */
    @Override
    public void handleTransportError(WebSocketSession session, Throwable exception) throws Exception {
        super.handleTransportError(session, exception);
    }
}
