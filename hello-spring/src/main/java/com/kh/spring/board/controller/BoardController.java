package com.kh.spring.board.controller;


import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.kh.spring.board.model.service.BoardService;
import com.kh.spring.board.model.vo.Attachment;
import com.kh.spring.board.model.vo.Board;
import com.kh.spring.board.model.vo.BoardExt;
import com.kh.spring.common.util.HelloSpringUtils;

import lombok.extern.slf4j.Slf4j;

@Controller
@RequestMapping("/board")
@Slf4j
public class BoardController {
	@Autowired
	private ServletContext application;
	
	@Autowired
	private ResourceLoader resourceLoader;

	@Autowired
	private BoardService boardService;

	@GetMapping("/boardList.do")
	public String boardList(@RequestParam(required = true, defaultValue = "1") int cPage, Model model,
			HttpServletRequest request) {

		try {
			// 기존 페이지 계산식 응용
			// int end = cPage * numPerPage;
			// int start = (cPage - 1) * numPerPage + 1;

			log.debug("cPage = {}", cPage);
			final int limit = 10;
			final int offset = (cPage - 1) * limit;
			Map<String, Object> param = new HashMap<>();
			param.put("limit", limit);
			param.put("offset", offset);
			// 1.업무로직 : content영역 - rowbounds
			List<Board> list = boardService.selectBoardList(param);

			// 총게시글
			int totalContents = boardService.selectBoardCount();
			// 게시판 주소
			String url = request.getRequestURI();
			// 유틸에 만들어둔 메소드 호출
			String pageBar = HelloSpringUtils.getPageBar(cPage, limit, totalContents, url);

			log.debug("list = {}", list);
			log.debug("totalContents = {}", totalContents);
			log.debug("pageBar = {}", pageBar);
			model.addAttribute("pageBar", pageBar);
			model.addAttribute("list", list);
		} catch (Exception e) {
			log.error("게시글 조회 오류!", e);
			throw e;
		}
		return "board/boardList";
	}

	@GetMapping("/boardForm.do")
	public void boardForm() {

	}

	@PostMapping("/boardEnroll.do")
	public String boardEnroll(@ModelAttribute BoardExt board, @RequestParam(name = "upFile") MultipartFile[] upFiles, RedirectAttributes redirectAttr)
			throws IllegalStateException, IOException {

		try {
		log.debug("board = {}", board);
		// 1. 파일 저장 : 절대경로 /resources/upload/board
		// 생명주기순 짧 - > 긴
		// pageContext:PageContext - request:HttpServletRequest - session:HttpSession -
		// application:ServletContext

		String saveDirectory = application.getRealPath("/resources/upload/board");
		
		log.debug("saveDirectory = {}", saveDirectory);
		log.debug("path = {}", application.getContextPath());
		// 디렉토리 생성
		File dir = new File(saveDirectory);
		if (!dir.exists()) {
			dir.mkdirs();// 복수개의 디렉토리를 생성
		}

		List<Attachment> attachList = new ArrayList<>();

		for (MultipartFile upFile : upFiles) {
			// input[name=upFile]로부터 비어있는 upFile이 넘어온다.
			if (upFile.isEmpty()) {
				continue;
			}
			String renamedFilename = HelloSpringUtils.getRenamedFilename(upFile.getOriginalFilename());
			// a.서버컴퓨터에 저장
			File dest = new File(saveDirectory, renamedFilename);
			upFile.transferTo(dest);// 파일 이동

			// b.저장된 데이터를 Attachment객체에 저장 및 list에 추가
			Attachment attach = new Attachment();
			attach.setOriginalFilename(upFile.getOriginalFilename());
			attach.setRenamedFilename(renamedFilename);
			attachList.add(attach);
		}
		log.debug("attachList = {}", attachList);
		//board객체에 설정
		board.setAttachList(attachList);
		
		// 2. 업무로직 : db저장 board, attachment
				
		int result = boardService.insertBoard(board);
		
		// 3. 사용자피드백 & 리다이렉트
		redirectAttr.addFlashAttribute("msg", "게시글 등록 성공!");
		}catch (Exception e) {
			log.error("게시글 등록 오류!", e);
			throw e;
		}
		return "redirect:/board/boardDetail.do?no="+board.getNo();
	}
	
	@GetMapping("/boardDetail.do")
	public void selectOneBoard(@RequestParam int no, Model model) {
		//1. 업무로직
		log.debug("no = {}", no);
		
		//BoardExt board = boardService.selectOneBoard(no);
		BoardExt board = boardService.selectOneBoardCollection(no);
		
		model.addAttribute("board",board);
		
	}
	/**
	 * ResponseEntity
	 * 1. status code 커스터마이징 404, ~~
	 * 2. 응답 header 커스터마이징
	 * 3. @ResponseBody 기능 포함 제네릭 타입을 쓰게됨
	 * @param no
	 * @return
	 * @throws UnsupportedEncodingException 
	 */
	@GetMapping("/fileDownload.do")
	public ResponseEntity<Resource> fileDownloadWithResponseEntity(@RequestParam int no) throws UnsupportedEncodingException{
		//1. 업무로직 : db조회
			Attachment attach = boardService.selectOneAttachment(no);
			ResponseEntity<Resource> responseEntity = null;
			
		try {
			//에러처리
			if(attach == null) {
				return ResponseEntity.notFound().build();
			}
		
			//2. Resource객체
			String saveDirectory = application.getRealPath("/resources/upload/board");
			File downFile = new File(saveDirectory, attach.getRenamedFilename());
			Resource resource = resourceLoader.getResource("file:" + downFile);
			String filename = new String(attach.getOriginalFilename().getBytes("utf-8"), "iso-8859-1");
			//3. ResponseEntity 객체 생성 및 리턴
			//builder패턴
			responseEntity =  
					//200번으로 설정
			ResponseEntity.ok().header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_OCTET_STREAM_VALUE)
			.header(HttpHeaders.CONTENT_DISPOSITION, "attachment;filename="+filename).body(resource);
		
		}catch(Exception e) {
			log.error("파일 다운로드 오류", e);
			throw e;
		}
		return responseEntity;
	}
	
	//@GetMapping(value = "/fileDownload.do", produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
	@ResponseBody//응답메세지에 return객체를 직접 출력
	public Resource fileDownload(@RequestParam int no, HttpServletResponse response) throws UnsupportedEncodingException {
		//1. 업무로직 : db에서 첨부파일 정보 조회
		Attachment attach = boardService.selectOneAttachment(no);
		log.debug("attach = {}", attach);
		if(attach ==null) {
			throw new IllegalArgumentException("해당 파일은 존재하지 않습니다. : " + no);
		}
		//2. resource객채를 리턴 : 응답메세지에서 출력은 spring container가 처리
		String originalFilename = attach.getOriginalFilename();
		String renamedFilename = attach.getRenamedFilename();
		String saveDirectory = application.getRealPath("/resources/upload/board");
		
		File downFile = new File(saveDirectory, renamedFilename);
		
		//웹상 자원, 서버컴퓨터 자원을 모두 다룰수 있는 스프링의 추상화 layer
		String location = "file:" + downFile.toString();
		
		//String location = "https://docs.oracle.com/javase/8/docs/api/java/lang/String.html";
		log.debug("location = {}", location);
		
		Resource resource = resourceLoader.getResource(location);
		
		//3:응답헤더
		//한글 깨짐 방치 처리
		originalFilename= new String(originalFilename.getBytes("utf-8"), "iso-8859-1");
		
		//originalFilename = "String.html";
		
		
		//octet 이진테이터로 보낼때 사용 여기보다 매핑에서 하는 것이 우선 순위가 높다
		//response.setContentType(MediaType.APPLICATION_OCTET_STREAM_VALUE);
		
		response.addHeader(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename="+ originalFilename);
	
		return resource;
		
	}
	
	@GetMapping("/boardSearch.do")
	public ResponseEntity <Map<String, Object>> boardSearch(@RequestParam String search) {
		log.debug("searchKeyword = {}", search);
		
		List<BoardExt> list = boardService.searchBoardList(search);
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("searchList", list);
		map.put("search", search);
		return ResponseEntity.ok().header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_UTF8_VALUE)
				.body(map);
	}
	
	//map으로
	/*
	 * @GetMapping("/boardSearch.do") public Map<String, Object>
	 * searchTitle(@RequestParam String search) { log.debug("searchKeyword = {}",
	 * search); //board 객체 사용해도 상관없음 List<BoardExt> list =
	 * boardService.searchBoardList(search); Map<String, Object> map = new
	 * HashMap<String, Object>(); map.put("searchList", list); map.put("search",
	 * search); return map; }
	 */
	
	
}
