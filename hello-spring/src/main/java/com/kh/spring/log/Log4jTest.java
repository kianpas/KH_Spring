package com.kh.spring.log;

import org.apache.log4j.Logger;


/**
 * Logging
 *  - 콘솔로그 : sout 보다 효율적인 로그관리가 가능.
 *  - 파일로그
 *  
 * Level(Priority) 우선순위
 *  - fatal : 아주 심각한 에러 발생
 *  - error : 요청 처리중 오류 발생
 *  - warn : 경고성 메세지. 현재 실행에는 문제가 없지만, 향후 잠재적 오류가 될 가능성이 있음
 *  		 deprecated(사라질 예정)
 *  - info : 요청 처리중 상태변경등의 정보성 메세지
 *  - debug : 개발중에 필요한 로그. 운영상에는 필요없음
 *  - trace : 개발용. debug의 범위를 한정해서 로깅할때.
 * @author 1
 *
 * 순수하게 log4j만 테스트한 것
 */
public class Log4jTest {
	
	private static final Logger log = Logger.getLogger(Log4jTest.class);
	
	public static void main(String[] args) {
		log.fatal("fatal");
		log.error("error");
		log.warn("warn");
		log.info("info");
		//여기까지 밖에 안나옴 
		//log4j에서 따로 설정해줘야 디버그, 트레이스 나옴
		
		log.debug("debug");
		log.trace("trace");
		
	}
}
