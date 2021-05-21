package com.kh.spring.demo.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.kh.spring.demo.model.service.DemoService;
import com.kh.spring.demo.model.validator.DevValidator;
import com.kh.spring.demo.model.vo.Dev;

/**
 * 사용자 요청 하나당 이를 처리하는 Controller메소드(Handler)가 하나씩 존재한다.
 * 
 * <h1>handler메소드가 가질 수 있는 매개변수</h1> html 사용가능
 * 
 * HttpServletRequest HttpServletResponse HttpSession java.util.Locale : 요청에 대한
 * Locale InputStream/Reader : 요청에 대한 입력스트림 OutputStream/Writer : 응답에 대한 출력스트림.
 * ServletOutputStream, PrintWriter
 * 
 * @PathVariable: 요청url중 일부를 매개변수로 취할 수 있다.
 * @RequestParam
 * @RequestHeader
 * @CookieValue
 * @RequestBody 뷰에 전달할 모델 데이터 설정 ModelAndView Model ModelMap
 * @ModelAttribute : model속성에 대한 getter
 * @SessionAttribute : session속성에 대한 getter SessionStatus: @SessionAttribute로
 *                   등록된 속성에 대하여 사용완료(complete)처리 Command객체 : http요청 파라미터를
 *                   커맨드객체에 저장한 VO객체 Error, BindingResult : Command객체에 저장결과,
 *                   Command객체 바로 다음위치시킬것. 기타 MultipartFile : 업로드파일 처리 인터페이스.
 *                   CommonsMultipartFile RedirectAttributes : DML처리후 요청주소 변경을
 *                   위한 redirect를 지원
 *                   
 * Command객체 : http요청 파리미터를 커맨드객체에 저장한 vo객체
 * @Valid 커맨드객체 유효성 검사용
 * Error, BindngResult : Command객체에 저장결과, Command객체 바로 다음 위치시킬것.                 
 */
@Controller
@RequestMapping("/demo")
public class DemoController {

	/**
	 * spring용 logging클래스
	 */
	private static final Logger log = LoggerFactory.getLogger(DemoController.class);

	@Autowired
	private DemoService demoService;

	/**
	 * 사용자 요청을 처리하는 handler
	 * 
	 * @return
	 */

	@RequestMapping("/devForm.do")
	public String devForm() {
		log.info("/demo/devForm.do요청");
		// viewResolver빈에 의해서 /WEB-INF/views + demo/devForm + .jsp jsp파일로 위임
		return "demo/devForm";
	}

	@RequestMapping("/dev1.do")
	public String dev1(HttpServletRequest request, HttpServletResponse response) {

		String name = request.getParameter("name");
		int career = Integer.valueOf(request.getParameter("career"));
		String email = request.getParameter("email");
		String gender = request.getParameter("gender");
		String[] lang = request.getParameterValues("lang");

		Dev dev = new Dev(career, name, career, email, gender, lang);
		// dev가 괄호안에 들어가 출력
		log.info("dev = {}", dev);

		// 2. 업무로직

		// 3. jsp에 출력
		request.setAttribute("dev", dev);
		return "demo/devResult";
	}

	/**
	 * name값과 일치하는 매개변수에 전달 
	 * 1.name값(userName)이 매개변수(name)와 일치하지 않는다면,
	 * name="userName" 지정 name 값이 userName 담김 (name속성값이 매개변수명보다 우선순위가 높음)
	 * 
	 * 2.required=true가 기본값, 무조건 값을 요구함, false 가능
	 * 사용자가 선택하는 필드는 false로 명시할 것
	 *    
	 * 3.defaultValue = "" 기본값 지정
	 * 지정한 경우, 값이 없거나, 형변환 오류가 발생해도 기본값으로 정상처리된다.
	 * 숫자인 경우 0이 아닌 빈문자열이 넘어감. 형변환 오류 가능성 존재(400)
	 * @param name
	 * @param career
	 * @param email
	 * @param gender
	 * @param lang
	 * @param model
	 * @return
	 */
	@RequestMapping("/dev2.do")
	public String dev2(@RequestParam String name, @RequestParam(defaultValue = "1") int career,
			@RequestParam String email, @RequestParam String gender, @RequestParam(required = false) String[] lang,
			Model model) {
		Dev dev = new Dev(career, name, career, email, gender, lang);
		log.info("dev = {}", dev);

		// 3. jsp에 위임
		model.addAttribute("dev", dev); // jsp에서 scope="request"에 저장되어있음

		return "demo/devResult";
	}
	
	/**
	 * 매개변수 Dev객체를 커맨드 객체라 한다. (컨 + C가 복사와 같다라는 개념) 연결시킨것
	 * jsp의 값을 그대로 Dev객체에 대입
	 * @ModelAttribute 모델에 등록된 속성을 가져오는 어노테이션
	 * Dev객체는 handler도착전에 model에 등록되어 있다.
	 * 
	 * @ModelAttribute은 생략이 가능하다
	 * 
	 * @param dev
	 * @return
	 * method=RequestMethod.POST는 post로 메소드 제한 거는 것
	 * {}로 배열을 받을 수 있다.
	 */
	@RequestMapping(value = "/dev3.do", method = {RequestMethod.POST})
	public String dev3(@ModelAttribute Dev dev) {
		log.info("dev={}", dev);
		
		return "demo/devResult";
	}
	
	//유효성검사
	@RequestMapping(value = "/dev4.do", method = {RequestMethod.POST})
	public String dev4(@Valid Dev dev, BindingResult bindingResult) {
		log.info("dev={}", dev);
		if(bindingResult.hasErrors()) {
			String errors = "";
			List<ObjectError> errorList = bindingResult.getAllErrors();
			for(ObjectError err : errorList) {
				errors += "{"+err.getCode()+":"+err.getDefaultMessage() + "}";
			}
			throw new IllegalArgumentException(errors);
		}
		
		return "demo/devResult";
	}
	
	//추가해줘야 유효성이 실행됨
	@InitBinder
	public void initBinder(WebDataBinder binder) {
		binder.setValidator(new DevValidator());
	}
	
	/**
	 * RedirectAttributes
	 * @param dev
	 * @param redirectAttr
	 * @return
	 */
	@RequestMapping(value = "/insertDev.do", method = RequestMethod.POST)
	public String insertDev(Dev dev, RedirectAttributes redirectAttr) {
		log.info("dev={}", dev);
		
		try {
			//1. 업무로직
			int result = demoService.insertDev(dev);
			
			//2. 사용자 피드백 & 리다이렉트, 플래시전달
			redirectAttr.addFlashAttribute("msg", "dev 등록 성공");
		}catch(Exception e) {
			log.error("dev 등록 오류!", e); //에러로그
			throw e;
		}
		//리다이렉트 처리
		return "redirect:/demo/devList.do";
	}
	
	//값 전달하기위해 model 넘김
	@RequestMapping(value = "/devList.do", method = RequestMethod.GET)
	public String devList(Model model) {
		
		//1.업무로직
		List<Dev> list = demoService.selectDevList();
		log.info("list = {}", list);
		
		//2.jsp위임
		model.addAttribute("list", list);
		
		return "demo/devList";
	}
	
	//@RequestMapping(value ="/updateDev", method=RequestMethod.GET)
	@GetMapping("/updateDev") //겟방식만 맵팽
	public String devUpdateForm(@RequestParam String no, Model model) {
		//@RequestParam(name = "no", required = true) 로 에러체크 가능
		//파라미터의 순서는 상관없다, 모델이 먼저 나와도 상관없음
		
		log.info("no = {}", no);
		try {
		Dev dev = demoService.selectOneDev(no);
		if(dev == null)
			throw new IllegalArgumentException("존재하지 않는 개발자 정보 : " + no);
		
		log.info("dev = {}", dev);
		model.addAttribute("dev", dev);
		}catch(Exception e) {
			log.error("dev 수정페이지 오류!", e);
			throw e;
		}
		return "demo/devUpdateForm";
	}
	
	@RequestMapping(value ="/updateDev.do", method=RequestMethod.POST)
	public String devUpdate(Dev dev, RedirectAttributes redirectAttr) {
		
		log.info("dev = {}", dev);
		try {
		int result = demoService.updateDev(dev);
		redirectAttr.addFlashAttribute("msg", "dev 수정 성공");
		}catch(Exception e) {
			log.error("dev 수정 오류!", e); //에러로그
			throw e;
		}
		return "redirect:/demo/devList.do";
	}
	
	@RequestMapping(value ="/deleteDev.do", method=RequestMethod.POST)
	public String deleteDev(@RequestParam String no, RedirectAttributes redirectAttr) {
		log.info("no = {}", no);
		try {
			int result = demoService.deleteDev(no);
			redirectAttr.addFlashAttribute("msg", "dev 삭제 성공");
			}catch(Exception e) {
				log.error("dev 삭제 오류!", e); //에러로그
				throw e;
			}
			return "redirect:/demo/devList.do";
	}
}
