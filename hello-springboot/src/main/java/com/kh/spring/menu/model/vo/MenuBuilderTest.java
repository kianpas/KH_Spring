package com.kh.spring.menu.model.vo;



public class MenuBuilderTest {

	public static void main(String[] args) {
		
		MenuBuilderTest mt = new MenuBuilderTest();
		mt.test1();
		
	}

	private void test1() {
		//1. 기본 생성자 + setter
		Menu m = new Menu();
		m.setId(1);
		m.setName("도토리묵");
		
		//2. 파라미터생성자
		Menu m1 = new Menu(0, null, null, 0, null, null);
		
		//3. Builder패턴
		Menu m2 = Menu.builder().id(1).name("도토리묵").restaurant("다람쥐네").price(8000).build();
		
	}

}
