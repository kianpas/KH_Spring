package com.kh.spring.websocket.config;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class WebsocketHandler extends TextWebSocketHandler{
	
	/**
	 * multi-thread에서 동기화를 지원하는 list
	 */
	List<WebSocketSession> sesssionList =  new CopyOnWriteArrayList<>(); 
	
	/**
	 * websocket 연혈 후 호출
	 * onopen
	 */
	@Override
	public void afterConnectionEstablished(WebSocketSession session) throws Exception {
		log.debug("onopen({}) : {}", sesssionList.size(), session);
		sesssionList.add(session);
	}
	
	/**
	 * client가 message를 전송한 경우 호출
	 * onmessage
	 */
	@Override
	protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
		log.debug("onmessage : {} from {}", message, session);
		
		String sender = session.getId();
		
		for(WebSocketSession sess : sesssionList) {
			TextMessage msg = new TextMessage(sender + " : " + message.getPayload());
			sess.sendMessage(msg);
		}
	}

	/**
	 * websocket 연결 해재후 호출
	 * onopen
	 */
	@Override
	public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
		log.debug("onclose({}) : {}", sesssionList.size(), session);
		sesssionList.remove(session);
	}

	
}
