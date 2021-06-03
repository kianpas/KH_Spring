package com.kh.spring.menu.model.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Menu {
	
	private int id;
	private String restaurant;
	private String name;
	private int price;
	private MenuType type; //kr, ch, jp
	private String taste; //mild, hot

}
