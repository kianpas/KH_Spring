package com.kh.spring.log;



import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * PSA Portable Service Abstraction 이동가능한 서비스추상체
 * 
 * Slf4j - 스프링이 제공하는 logging 추상체
 * 
 * 구현체
 * 1. log4j
 * 2. java.util.logging
 * 3. apache loggin
 * 4. logback
 *	  
 *    spring app -> slf4j -> log4j
 *    slf4j로 로그백하는 경우 시간감소
 *    log4j는 오래걸림
 *    
 */
public class Slf4jTest {
	
	private static final Logger log = LoggerFactory.getLogger(Slf4jTest.class);
	
	public static void main(String[] args) {
		
		//log.fatal("fatal"); slf4j 는 fatal 없음
		log.error("error");
		log.warn("warn");
		log.info("info");
		//여기까지 밖에 안나옴 
		
		log.debug("debug");
		log.trace("trace");
	}
}
