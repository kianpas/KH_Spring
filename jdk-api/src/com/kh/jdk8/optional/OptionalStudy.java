package com.kh.jdk8.optional;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import javax.management.RuntimeErrorException;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 
 * Optional
 *  - 존재할 수도 있고, 안할 수도 있는 객체를 감싸고 있는 wrapper객체
 *  - 변수명에 maybeMember, optUser와 같이 Optional타입을 암묵적으로 제시한다.
 *  
 *  1. Optional.empty() : 비어있는 Optional객체
 *  2. Optional.of(obj) : 결코 null이 아닌 객체를 담은 Optional 객체
 *  3. Optional.ofNullable(obj) : null일 수도 있는 Optional 객체
 *  
 *  - get()
 *  - orElse(T)
 *  - orElseGet(Supplier)
 *  - orElseThrow(Supplier)
 *  - isPresent:boolean
 *  - ifPresent(Consumber)
 *  
 *  
 * if(s != null)
 * 		s.foo()
 *
 */
public class OptionalStudy {
	
	public static void main(String[] args) {
		OptionalStudy study = new OptionalStudy();
		//study.test1();
		study.test2();
	}

	private void test2() {
		
		getEmailOfMemberFromOrder(null);
		getEmailOfMemberFromOrder(new Order());
		Member member =new Member();
		member.setEmail("honggd@naver.com");
		Order order = new Order();
		order.setMember(member);
		System.out.println(getEmailOfMemberFromOrder(null));
		System.out.println(getEmailOfMemberFromOrder(new Order()));
		
		
	}
	
	public String getEmailOfMemberFromOrder(Order order) {
		
		return Optional.ofNullable(order).map(Order::getMember)
			.map(Member::getEmail).orElse("메일이 존재하지 않습니다.");
		
	}
	
	@Data
	@NoArgsConstructor
	static class Order {
		Member member;
		int count;
	}
	
	@Data
	@NoArgsConstructor
	static class Member {
		String id;
		String email;
	}

	private void test1() {
		List<String> list = Arrays.asList("홍길동", "신사", null, "");
		//null이 있으므로 null 에러 발생
		list.stream().forEach(s->System.out.println(s + " : " + getStringLength(s)));
		
	}

	private int getStringLength(String str) {
		//str이 null일 경우 optional 처리
		Optional <String> maybeStr = Optional.ofNullable(str);
		//문자열의 길이 리턴, 위이 optional에 의해 orElse에 지정한 값이 출력됨
		return maybeStr.map(s->s.length())
				//.orElse(0);
				.orElseThrow(()-> new RuntimeException(str));
	}

}
