package com.kh.spring.memo.controller;

import java.beans.PropertyEditor;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.SessionAttribute;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.kh.spring.memo.model.service.MemoService;
import com.kh.spring.memo.model.vo.Memo;

import lombok.extern.slf4j.Slf4j;

@Controller
@Slf4j
@RequestMapping("/memo")
@SessionAttributes({"next"})
public class MemoController {

	@Autowired
	private MemoService memoService;

	@GetMapping("/memo.do")
	public ModelAndView selectMemoList(ModelAndView mav) {
		
		//log.debug("memoService = {}", memoService.getClass()); 프록시확인용
		
		//1. 업무로직 : memo목록조회
		List<Memo> list = memoService.selectMemoList();
		log.info("list = {}", list);
		
		//2.jsp에 위임
		mav.addObject("list", list);
		mav.setViewName("memo/memo");
		return mav;
	}

	@PostMapping("/insertMemo.do")
	public String insertMemo(Memo memo, RedirectAttributes redirectAttr, @SessionAttribute(required=false) String next) {

		log.info("memo = {}", memo);
		log.info("next = {}", next);
		try {
			int result = memoService.insertMemo(memo);
			redirectAttr.addFlashAttribute("msg", "메모 등록 성공!");
		} catch (Exception e) {
			log.error("메모 입력 오류!", e);
			throw e;
		}

		return "redirect:/memo/memo.do";
	}
	
	@PostMapping("/deleteMemo.do")
	public String deleteMemo(int no, RedirectAttributes redirectAttr) {
		log.info("no = {}", no);
		try {
		int result = memoService.deleteMemo(no);
		} catch(Exception e) {
			log.error("메모 삭제 오류!", e);
			throw e;
		}
		return "redirect:/memo/memo.do";
	}
	
	
	/**
	 * 날짜포맷 변경 jsp에서 사용
	 * @param binder
	 */
	@InitBinder
	public void initBinder(WebDataBinder binder) {
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		PropertyEditor editor = new CustomDateEditor(format, true);
		binder.registerCustomEditor(Date.class, editor);
	}

}
