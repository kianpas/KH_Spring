package com.kh.spring.member.controller;

import java.beans.PropertyEditor;
import java.sql.Date;
import java.text.SimpleDateFormat;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttribute;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.kh.spring.member.model.service.MemberService;
import com.kh.spring.member.model.vo.Member;

import lombok.extern.slf4j.Slf4j;

@Controller
@RequestMapping("/member")
@Slf4j
@SessionAttributes({"loginMember","next"})
public class MemberController {
	
	//private static final Logger log = LoggerFactory.getLogger(MemberController.class);
	@Autowired
	private MemberService memberService;
	
	@Autowired
	private BCryptPasswordEncoder bCryptPasswordEncoder;
	
	@GetMapping("/memberEnroll.do")
	public void memberEnroll() {
		// /WEB-INF/views/member/memberEnroll.js로 자동포워딩됨
		// RequestToViewNameTranslator빈이 요청주소에서 viewName을 유추함
	}
	@PostMapping("/memberEnroll.do")
	public String memberEnroll(Member member, RedirectAttributes redirectAttr) {
		
		log.info("member = {}", member);
		try {
	
			//0. 비밀번호 암호화 처리
			String rawPassword = member.getPassword();
			String encodedPassword = bCryptPasswordEncoder.encode(rawPassword);
			member.setPassword(encodedPassword);
			
			log.info("member(암호화처리이후) = {}", member);
			//1. 업무로직
			
		int result = memberService.insertMember(member);
		
		//2. 사용자피드백
		redirectAttr.addFlashAttribute("msg", "회원가입성공");
		
		}catch(Exception e) {
			log.error("회원가입 오류!", e);
			throw e;
		}
		
		return "redirect:/";
	}
	
	//공란 처리 메소드
	/**
	 * java.sql.Date, java.util.Date 필드에 값대입시 
	 * 사용자 입력값이 ""인 경우, null로 처리될 수 있도록 설정
	 * @param binder
	 */
	@InitBinder
	public void initBinder(WebDataBinder binder) {
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		PropertyEditor editor = new CustomDateEditor(format, true);
		binder.registerCustomEditor(Date.class, editor);
	}
	
	//로그인페이지 넘어가면서 세션에 레퍼러가 저장됨
	@GetMapping("/memberLogin.do")
	public void memberLogin(@RequestHeader(name="Referer", required=false) String referer, Model model) {
		log.info("referer = {}", referer);
		if(referer !=null)
			model.addAttribute("next", referer); // sessionScope에 저장
	}
	
	@PostMapping("/memberLogin.do")
	public String memberLogin(@RequestParam(value = "id") String id, 
							@RequestParam(value = "password") String password, 
							@SessionAttribute(required=false) String next, 
							RedirectAttributes redirectAttr, 
							Model model) {
		
		//1. 업무로직
		Member member = memberService.selectOneMember(id);
		log.info("Member = {}", member);
		
		//2. 로그인여부 분기처리
		//기존 패스워드, 암호화된 패스워드
		if(member!=null && bCryptPasswordEncoder.matches(password, member.getPassword())) {
						
			redirectAttr.addFlashAttribute("msg", "로그인 성공");
			
			//세션에 멤버 저장, 모델이 request, session둘다 저장 가능
			//loginMember를 세션속성으로 저장하려면, class에 @SessionAttributes로 등록
			model.addAttribute("loginMember", member);			
		} else {
			//로그인 실패
			redirectAttr.addFlashAttribute("msg", "아이디 또는 비밀번호가 다릅니다.");
		}
		//사용한 next값은 삭제
		model.addAttribute("next", null);		
		
		return "redirect:"+(next!=null? next:"/");
	}
	
	/**
	 * @sessionAttributes를 통해 등록한 session속성은
	 * SessionStatus객체에 대해서 complete처리해야 한다.
	 * @return
	 */
	@GetMapping("/memberLogout.do")
	public String memberLogout(SessionStatus status) {
		if(!status.isComplete())
			status.setComplete();
		return "redirect:/";
	}
}
