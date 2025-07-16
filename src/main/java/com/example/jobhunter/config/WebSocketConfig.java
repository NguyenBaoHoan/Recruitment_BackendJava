package com.example.jobhunter.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    @Override
    public void configureMessageBroker(MessageBrokerRegistry config) {
        // Destination prefix cho client gửi message đến server (e.g., /app/chat)
        config.setApplicationDestinationPrefixes("/app");
        // Destination prefix cho server gửi message đến client (broker)
        config.enableSimpleBroker("/topic", "/user");
        // Prefix để gửi tin nhắn riêng tư
        config.setUserDestinationPrefix("/user");
    }

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        // Endpoint để client kết nối WebSocket, "/ws" là endpoint
        registry.addEndpoint("/ws").setAllowedOriginPatterns("*");
    }
}
