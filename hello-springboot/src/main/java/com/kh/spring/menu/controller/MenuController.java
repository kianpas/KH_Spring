package com.kh.spring.menu.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.kh.spring.menu.model.service.MenuService;
import com.kh.spring.menu.model.vo.Menu;
import com.kh.spring.menu.model.vo.MenuType;

import ch.qos.logback.core.status.Status;
import lombok.extern.slf4j.Slf4j;

/**
 * @RestController = @Controller + @ResponseBody
 * 
 *                 모든 handler는 @ResponseBody로 처리된다
 * 
 *
 */
//@CrossOrigin
@RestController
@Slf4j
public class MenuController {

	@Autowired
	private MenuService menuService;

	@GetMapping("/menus")
	public List<Menu> menus(HttpServletResponse response) {
		try {
			// 1. 업무로직
			List<Menu> list = menuService.selectMenuList();
			log.debug("list = {}", list);

			// 2. 리턴하면 @ResponseBody에 의해서 json변환 후, 응답출력 처리
			
			// 3. 응답헤더에 Access-Control-Allow-Origin : 특정 origin
			//response.setHeader(HttpHeaders.ACCESS_CONTROL_ALLOW_ORIGIN, "http://localhost:9090");
			return list;
		} catch (Exception e) {
			log.error("/menu 에러", e);
			throw e;
		}
	}

	/**
	 * @PathVariable 경로변수
	 * @return
	 */
	@GetMapping("/menus/{type}/{taste}")
	public List<Menu> menusByType(@PathVariable String type, @PathVariable String taste) {
		try {
			log.debug("type = {}, taste = {}", type, taste); // jsp type값이 넘어옴
			Map<String, Object> param = new HashMap<>();
			param.put("type", type);
			param.put("taste", taste);
			// 1. 업무로직
			List<Menu> list = menuService.selectMenuListByTypeAndTaste(param);
			log.debug("list = {}", list);

			// 2. 리턴하면 @ResponseBody에 의해서 json변환 후, 응답출력 처리
			return list;
		} catch (Exception e) {
			log.error("/menu/type 에러", e);
			throw e;
		}
	}

	// 잘못된 방식, 하나의 객체로 받는게 효율적
	@PostMapping("/menus/{restaurant}/{name}/{price}/{type}/{taste}")
	public int insertMenu(@PathVariable String restaurant, @PathVariable String name, @PathVariable int price,
			@PathVariable String type, @PathVariable String taste) {

		Menu menu = new Menu(price, restaurant, name, price, MenuType.menuTypeValueOf(type), taste);
		log.debug("menu = {}", menu);

		int result = menuService.insertMenu(menu);

		return result;
	}

	/**
	 * @Requestbody 요청 메세지의 바디에 작성된 json문자열을 자바객체로 변환 요청하는 것
	 * @param menu
	 * @return
	 */
	@PostMapping("/menu")
	public Map<String, Object> insertMenu(@RequestBody Menu menu) {

		try {

			log.debug("menu = {}", menu);
			int result = menuService.insertMenu(menu);
			Map<String, Object> map = new HashMap<String, Object>();
			//맵은 메시지 전달을 위한 것
			map.put("msg", "메뉴 등록 성공!");
			return map;

		} catch (Exception e) {

			log.error("메뉴 등록 실패!", e);
			throw e;
		}

	}
	
//	@GetMapping("/menu/{no}")
//	public Map<String, Object> selectOneMenu(@PathVariable int no){
//		
//		try {
//			log.debug("no = {}", no);
//			Menu menu = menuService.selectOneMenu(no);
//			log.debug("menu = {}", menu);
//			Map<String, Object>map = new HashMap<String, Object>();
//			map.put("menu", menu);
//			
//			return map;
//		} catch (Exception e) {
//			log.error("메뉴 조회 실패", no);
//			throw e;
//		}
//	}
	
	/**
	 * ResponseEntity를 통해서
	 * 존재하지 않는 메뉴번호를 요청한 경우
	 * 404 status code를 응답.
	 * 
	 * @param no
	 * @return
	 */
	@GetMapping("/menu/{no}")
	public ResponseEntity<Menu> selectOneMenu(@PathVariable int no){
		
		try {
			log.debug("no = {}", no);
			Menu menu = menuService.selectOneMenu(no);
			log.debug("menu = {}", menu);
			if(menu!=null) {
				return ResponseEntity.ok().body(menu);
			} else {
				return ResponseEntity.notFound().build();
			}
			
		} catch (Exception e) {
			log.error("메뉴 조회 실패", no);
			throw e;
		}
	}
	@PutMapping("/menu/{id}")
	public Map<String, Object> updateMenu(@RequestBody Menu menu){
		try {
			log.debug("menu = {}", menu);
			int result = menuService.updateMenu(menu);
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("msg", "메뉴 수정 성공!");
			return map;
		} catch (Exception e) {
			log.error("메뉴 수정 실패!", e);
			throw e;
		}
	}
	
//	@DeleteMapping("/menu/{id}")
//	public Map<String, Object> deleteMenu(@PathVariable String id){
//		
//		try {
//			log.debug("id = {}", id);
//			int result = menuService.deleteMenu(id);
//			
//			Map<String, Object> map = new HashMap<String, Object>();
//			map.put("msg", "메뉴 삭제 성공!");
//			return map;
//		} catch (Exception e) {
//			log.error("메뉴 삭제 실패!", e);
//			throw e;
//		}
//	}
	
	/**
	 * ResponseEntity 방식
	 * 1. builder 패턴, 메소드 체인
	 * 2. 생성자 방식
	 * 
	 * @param id
	 * @return
	 */
	@DeleteMapping("/menu/{id}")
	public ResponseEntity<?> deleteMenu(@PathVariable String id){
		
		try {
			log.debug("id = {}", id);
			int result = menuService.deleteMenu(id);
			
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("msg", "메뉴 삭제 성공!");
			
			if(result>0) {
				//ResponseEntity는 빌드 대신 new 도 가능
				return new ResponseEntity<Map<String, Object>>(map, HttpStatus.OK);
			} else {
				//404가 넘어감
				return new ResponseEntity<>(HttpStatus.NOT_FOUND);
			}
			
		} catch (Exception e) {
			log.error("메뉴 삭제 실패!", e);
			throw e;
		}
	}
}
