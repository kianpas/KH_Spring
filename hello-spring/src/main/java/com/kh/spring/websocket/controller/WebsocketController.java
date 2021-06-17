package com.kh.spring.websocket.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.kh.spring.websocket.model.vo.Notice;

import lombok.extern.slf4j.Slf4j;

@Controller
@RequestMapping("/ws")
@Slf4j
public class WebsocketController {
	
	@Autowired
	private SimpMessagingTemplate simpMessagingTemplate;

	@GetMapping("/websocket.do")
	public void websocket() {
		
	}
	
	@GetMapping("/sockjs.do")
	public void sockJs() {
		
	}
	
	@GetMapping("/stomp.do")
	public void stomp() {
		
	}
	
	@GetMapping("/someRequest.do")
	@ResponseBody
	public Map<String, Object> someRequest() {
		//MessageBroker 이용하기
		Notice notice = new Notice();
		notice.setFrom("Foo어페럴");
		notice.setTo("honggd");
		notice.setMessage("Foo패럴의 신상품이 등록되었습니다.");
		notice.setType("NewProduct");
		notice.setTime(System.currentTimeMillis());
		
		simpMessagingTemplate.convertAndSend("/notice/honggd", notice);
		
		Map<String, Object> map = new HashMap<>();
		map.put("msg", "신상품이 정상 등록되었습니다. 신상품을 고객들에게 공지했습니다.");
		return map;
	}
}

