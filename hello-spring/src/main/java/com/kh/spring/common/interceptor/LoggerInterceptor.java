package com.kh.spring.common.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class LoggerInterceptor extends HandlerInterceptorAdapter{
	
	/**
	 * Handler 호출전에 실행
	 */
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		log.debug("================ start ==================");
		log.debug("request.getRequestURI() = {}", request.getRequestURI());
		log.debug("-----------------------------------------");
		
		return super.preHandle(request, response, handler); //무조건 true를 리턴
	}

	/**
	 * Handler 리턴이후에 실행
	 * (ModelAndView객체 확인 가능)
	 */
	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
			ModelAndView modelAndView) throws Exception {
		
		super.postHandle(request, response, handler, modelAndView);
		log.debug("-----------------------------------------");
		log.debug("modelAndView = {}", modelAndView);
	}
	
	/**
	 * view단(jsp) 
	 */
	@Override
	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
			throws Exception {
		log.debug("------------------view-----------------");
		super.afterCompletion(request, response, handler, ex);
		log.debug("____________________end__________________");
	}

	@Override
	public void afterConcurrentHandlingStarted(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		// TODO Auto-generated method stub
		super.afterConcurrentHandlingStarted(request, response, handler);
	}
	
	
}
