package com.example.demo.service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

@Service
public class ChatHandler extends TextWebSocketHandler {

    private final CopyOnWriteArrayList<WebSocketSession> sessions = new CopyOnWriteArrayList<>();
    private final List<String> messages = new ArrayList<>();
    
    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
       
        sessions.add(session);
        for (String message : messages) {
            session.sendMessage(new TextMessage(message));
        }
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        sessions.remove(session);
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        String payload = message.getPayload();
        String[] parts = payload.split(":");

        if (parts.length == 2) {
            String email = parts[0];
            String password = parts[1];

            String responseMessage = "";
            if ("123ABC".equals(password)) {
                responseMessage = email + " se conecto correctamente";
            } else {
                responseMessage = email + " se conecto incorrectamente";
            }

      
            messages.add(responseMessage);
            for (WebSocketSession webSocketSession : sessions) {
                webSocketSession.sendMessage(new TextMessage(responseMessage));
            }

        } else {
           
            session.sendMessage(new TextMessage("Mensaje no válido"));
        }
    }
}