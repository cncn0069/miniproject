package edu.pnu.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

import com.fasterxml.jackson.databind.ObjectMapper;

import edu.pnu.handler.WebSocketHandler;
import edu.pnu.service.ChatService;

@Configuration
@EnableWebSocket
public class WebSocketConfig implements WebSocketConfigurer {

	private final ObjectMapper mapper = new ObjectMapper();
	
	@Autowired
	private ChatService chatService;
	
	@Override
	public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
		// TODO Auto-generated method stub
		registry.addHandler(signalingSockertHandler(), "/ws/chat")
			.setAllowedOrigins("*");
	}
	
	@Bean
	public WebSocketHandler signalingSockertHandler() {
		return new WebSocketHandler(mapper, chatService);
	}

}
