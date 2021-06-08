package com.kh.spring.member.model.vo;

import java.sql.Date;
import java.util.Collection;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class Member implements UserDetails{

	private String id; //userName
	private String password; //password
	private String name;
	private String gender;
	private Date birthday;
	private String email;
	private String phone;
	private String address;
	private String[] hobby;
	private Date enrollDate;
	
	
	/**
	 * 복수개의 권한을 문자열로 관리
	 * 문자열 data("ROLE_USER", "ROLE_ADMIN")를 처리할 수 있는 GrantedAuthority의 하위 클래스
	 */
	private List<SimpleGrantedAuthority> authorities;
	
	private boolean enabled; //활성화 여부
	
	/**
	 * Collection - List/Set
	 * 
	 * Collection<? extends GrantedAuthority> 
	 * - GrantedAuthority를 상속하는 ? -> 자식타입 상한선을 정한 것
	 * - <? super Integer> -> Integer 부모타입 (하한선)
	 * Collection<GrandAuthority> 
	 */
	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		// TODO Auto-generated method stub
		return authorities;
	}
	@Override
	public String getUsername() {
		
		return id;
	}
	//계정 유효여부
	@Override
	public boolean isAccountNonExpired() {
		// TODO Auto-generated method stub
		return true;
	}
	//계정 잠김여부
	@Override
	public boolean isAccountNonLocked() {
		// TODO Auto-generated method stub
		return true;
	}
	//계정정보 유효여부
	@Override
	public boolean isCredentialsNonExpired() {
		// TODO Auto-generated method stub
		return true;
	}
		
	
}
