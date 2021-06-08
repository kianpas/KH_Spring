package com.kh.spring.member.controller;

import java.beans.PropertyEditor;
import java.security.Principal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.kh.spring.member.model.vo.Member;

import lombok.extern.slf4j.Slf4j;

@Controller
@Slf4j
@RequestMapping("/member")
public class MemberController {

	@GetMapping("/memberLogin.do")
	public void memberLogin() {}
	
	/**
	 * Authentication
	 *  - Principal : 인증된 사용자객체
	 *  - Credential : 인증에 필요한 비밀번호
	 *  - Authorities : 인증된 사용자가 가진 권한
	 */
//	@GetMapping("/memberDetail.do")
//	public void memberDetail(Model model) {
//		//1. SecurityContextHolder로 부터 가져오기
//		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//		Member principal = (Member) authentication.getPrincipal();
//		model.addAttribute("loginMember", principal);
//		
//		log.debug("authentication = {}", authentication);
//		log.debug("principal = {}", principal);
//	}
	
	/**
	 * 2. handlerMapping
	 */
	@GetMapping("/memberDetail.do")
	public void memberDetail(Authentication authentication, Model model) {
		//1. SecurityContextHolder로 부터 가져오기
		
		Member principal = (Member) authentication.getPrincipal();
		model.addAttribute("loginMember", principal);
		log.debug("authentication = {}", authentication);
		log.debug("principal = {}", principal);
	}
	
	@PostMapping("/memberUpdate.do")
	public String memberUpdate(Member updateMember, Authentication oldAuthentication, RedirectAttributes redirectAttr) {
		log.debug("updateMember = {}", updateMember);
		
		//1.업무로직 : db의 member객체를 수정
		
		//2.SecurityContext의 authentication 객체를 수정
		//기존 auth를 가져와서 사용
		//updateMember에 누락된 정보 password, authorities 추가
		updateMember.setPassword(((Member) oldAuthentication.getPrincipal()).getPassword());
		Collection<? extends GrantedAuthority> oldAuthorities = oldAuthentication.getAuthorities();
		
		List<SimpleGrantedAuthority> authorites =new ArrayList<>();
		for(GrantedAuthority auth: oldAuthorities) {
			//문자열로 처리위함
			SimpleGrantedAuthority simpleAuth = new SimpleGrantedAuthority(auth.getAuthority()); //문자열을 인자로 auth객체 생성
			authorites.add(simpleAuth);
		}
		updateMember.setAuthorities(authorites);
		
		//새로운 authentication 객체 생성
		Authentication newAuthentication 
		= new UsernamePasswordAuthenticationToken(updateMember, oldAuthentication.getCredentials(), oldAuthentication.getAuthorities());
		
		//securityContextHolder - securityContext 하위에 설정
		SecurityContextHolder.getContext().setAuthentication(newAuthentication);
		
		//3.사용자 리다이렉트 
		
		return "redirect:/member/memberDetail.do";
	}
	
	
	
	/**
	 * 커맨드객체 이용시 사용자입력값(String)을 특정필드타입으로 변환할 editor객체를 설정
	 * 
	 * @param binder
	 */
	@InitBinder
	public void initBinder(WebDataBinder binder) {
		//Member.birthday:java.sql.Date 타입 처리
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		
		//커스텀에디터 생성 : allowEmpty - true (빈문자열을 null로 변환처리 허용)
		PropertyEditor editor = new CustomDateEditor(sdf, true);
		binder.registerCustomEditor(java.sql.Date.class, editor);
	}
	
	
}
