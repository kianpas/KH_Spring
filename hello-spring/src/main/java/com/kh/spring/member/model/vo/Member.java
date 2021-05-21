package com.kh.spring.member.model.vo;

import java.sql.Date;

import org.springframework.stereotype.Component;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

//@ToString
//@Setter
//@Getter
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Member {
	
	
	private String id;
	private String password;
	private String name;
	private String gender;
	private Date birthday;
	private String email;
	private String phone;
	private String address;
	private String[] hobby;
	private Date enrollDate;
	private boolean enabled;  //회원 활성화 여부, db에는 0(false), 1(true)로 나눔
}
