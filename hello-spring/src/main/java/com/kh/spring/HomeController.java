package com.kh.spring;

import java.text.DateFormat;
import java.util.Date;
import java.util.Locale;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import lombok.extern.slf4j.Slf4j;

/**
 * Handles requests for the application home page.
 */
@Controller
@Slf4j
public class HomeController {
	
	/**
	 * 메소드 요청 get을 처리함
	 */
	@RequestMapping(value = "/", method = RequestMethod.GET)
	public String home() {
		log.info("index 페이지 요청!");
		
		// WEB-INF/views/home.jsp의미
		
		//forward 붙여주면 접두사, 접미사 붙이지 않고 그래도 사용
		return "forward:/index.jsp";
	}
	
}
