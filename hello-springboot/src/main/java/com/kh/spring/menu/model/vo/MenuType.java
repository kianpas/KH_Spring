package com.kh.spring.menu.model.vo;

import com.fasterxml.jackson.annotation.JsonValue;

public enum MenuType {
	
	KR("kr"),CH("ch"),JP("jp");
	
	private String value;
	
	/**
	 * enum 생성자의 접근 제한자는 private
	 * 외부에서 접근/생성 불가
	 * 
	 * @param value
	 */
	MenuType(String value) {
		this.value = value;
	}
	
//	json사용하도록 붙임 json이 찍힐 경우 상수가아니라 여기서 정의한 값을 가져가도록 함
//	jsonvalue를 붙이지 않았을 경우 상수 대문자 KR이 찍히고, 붙이면 "kr"ㅇ이 출력
	@JsonValue 
	public String getValue() {
		return this.value;
	}
	
	public static MenuType menuTypeValueOf(String value) {
		switch(value) {
		
		case  "kr" : return KR;
		case  "ch" : return CH;
		case  "jp" : return JP;
		default :
			throw new AssertionError("Unknown MenuType : " + value);
		
		}
		
	}
}
