package com.kh.jdk8.stream;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.IntSummaryStatistics;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Pattern;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.stream.DoubleStream;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

public class StreamStudy {

	public static void main(String[] args) {

		StreamStudy study = new StreamStudy();
		// study.test1();
		// study.test2();
		// study.test3();
		// study.test4();
		// study.test5();
		//study.test6();
		//study.test7();
		//study.test8();
		study.test9();
	}

	/**
	 * reduce
	 * 스트림의 요소로 연산처리후 하나의 결과값을 리턴
	 * 
	 * 펑션의 파생형
	 * BinaryOperator : 매개변수 2개와 리턴값이 자료형이 같은 Function
	 * UnaryOperator : 매개변수와 리턴값의 자료형이 같은 Function
	 */
	private void test9() {
		Integer result = 
			Arrays.asList(1,2,3,4,5,6,7,8,9,10)
			//identity, binary 선택
				  .stream().reduce(0, (sum, n) -> {
					  System.out.println("sum = " + sum + ", n = " + n);
					 return sum + n; 
				  });
		//System.out.println("result = " + result); //55라는 하나의 결과값을 리턴
		
		
		result = 
				Arrays.asList(1,2,30,4,15,67,7,8,9,10)
				//identity, binary 선택
					  .stream()
					  .reduce(0, (max, n) -> max> n? max:n);
		//배열의 최대값
		//System.out.println("max = " + result);
		
		List<Person> list = Arrays.asList(
				new Person("홍길동", 35), 
				new Person("신사임당", 40), 
				new Person("세종", 45), 
				new Person("홍난파", 80), 
				new Person("전달력", 69) 
			);
		
		Person maxAgePerson = 
			list.stream()
				 .reduce((p1, p2)-> p1.age > p2.age ? p1 : p2) //Optional<Person>
				 .get();
		
		System.out.println(maxAgePerson); //StreamStudy.Person(name=홍난파, age=80)
		
		Person person = 
			list.stream().reduce(new Person("", 0), (identity, p)-> {
				identity.age += p.age;
				identity.name +=p.name;
				return identity;
			});
		//값이 누적 전달됨, 타입이 동일하다는 약속이 있음
		System.out.println(person);  //StreamStudy.Person(name=홍길동신사임당세종홍난파전달력, age=269)
	}
	
	@AllArgsConstructor
	@NoArgsConstructor
	@Data
	static class Person {
		private String name;
		private int age;
		
	}
	

	/**
	 * 구구단
	 */
	private void test8() {
		//중괄호를 통해 2개의 변수를 전달
		IntStream
			.range(2, 10)
			.forEach(dan -> {
				IntStream.range(1, 10)
				.forEach(n ->{
					System.out.println(dan + " * " + n + " = " + (dan * n));
						if(n == 9) System.out.println();
				});
				
			});
		
	}

	/**
	 * 기본형 stream - IntStream - LongStream - DoubleStream
	 */
	private void test7() {
		int[] arr = { 1, 2, 3 };
		IntStream intStream = Arrays.stream(arr);

		DoubleStream doubleStream = DoubleStream.of(1.1, 2.3, 4.56);
		doubleStream.forEach(System.out::println);

		// range | rangeClosed
		// IntStream.range(1, 10).forEach(System.out::println); //1과 10는 포함되지 않음 2 ~ 9

		// IntStream.rangeClosed(1, 10).forEach(System.out::println); //1과 10는 포함

		int sum = IntStream.rangeClosed(1, 10).sum();
		System.out.println(sum); // 1 ~ 10까지의 합

		double avg = IntStream.rangeClosed(1, 100).average() // OptionalDouble 리턴
				.getAsDouble();
		System.out.println(avg); // 1 ~ 100의 평균 50.5

		IntSummaryStatistics summar = 
		IntStream.of(32, 50, 80, 77, 100, 27, 88)
				 .summaryStatistics();
		
		System.out.println(summar); //{count=7, sum=454, min=27, average=64.857143, max=100} 와 같은 정보 출력

	}

	/**
	 * anyMatch - 스트림의 요소중 Predicate결과가 하나라도 true이면 true 리턴
	 * 
	 * noneMatch - 스트림의 요소 모두가 Predicate에 만족하지 않으면 true 리턴
	 */
	private void test6() {
		boolean bool = Arrays.asList("a1", "b2", "c", "d4", "5").stream().anyMatch(s -> s.startsWith("a"));

		System.out.println("anyMatch = " + bool); // true

		bool = Arrays.asList("홍길동", "123", "가나다").stream().noneMatch(s -> Pattern.matches(".*[0-9].*", s));
		//.* 를 넣어서 한자리 이상으로 처리함, 없었을 경우 [0-9] 중 한자리만 있을경우에만 false 처리 
		
		System.out.println("noneMatch = " + bool); // 숫자가 하나라도 있을 경우 false 
	}

	/**
	 * collect
	 */
	private void test5() {
		List<Integer> list = Arrays.asList(1, 2, 3, 4, 5, 4, 3, 2, 1);
		List<Double> result1 = list.stream().map(n -> Math.pow(n, 2)).collect(Collectors.toList());
		System.out.println(result1);

		Set<Integer> result2 = list.stream().filter(n -> n % 2 == 0).collect(Collectors.toSet());

		System.out.println(result2);

		// 맵을 사용하여 값사용
		Map<Integer, String> result3 = list.stream().distinct().collect(Collectors.toMap(n -> n, n -> n + "" + n + n));
		System.out.println(result3);

	}

	/**
	 * stream의 처리 과정 1. collection, array로 부터 stream생성 2. 중간연산 Intermediate
	 * Operations : peek, filter, map 3. 단말연산 Terminal Operations : forEach, collect
	 * 중간, 단말 의 경우 리턴값이 stream인가 아닌가 차이. 리턴이 stream이면 중간
	 * 
	 * 최종단말연산 전까지는 중간연산을 완료하지 않는다
	 * 
	 * peek 중간에 보는 것 필터를 통과할 내용은 쭉 내려가서 forEach 출력 ex) 2는 peek에서 출력되고 필터를 지나서
	 * forEach로 출력됨
	 * 
	 */
	private void test4() {
		Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9, 10).stream().peek(n -> System.out.println("2의 배수인가 : " + n))
				.filter(n -> n % 2 == 0).peek(n -> System.out.println("4의 배수인가 : " + n)).filter(n -> n % 4 == 0)
				.forEach(System.out::println);

	}

	/**
	 * map 요소변경 a -> b
	 * 
	 * stream은 읽기전용으로 생성한다. 실제는 변화하지 않음
	 */
	private void test3() {
		List<Integer> list = Arrays.asList(1, 2, 3, 4, 5);

		list.stream().map(n -> n * 10).forEach(System.out::println); // 10 ~ 50

		List<Integer> another = list.stream().map(n -> n * 10).collect(Collectors.toList());

		System.out.println("list = " + list); // 1 ~ 5
		System.out.println("another = " + another); // 10 ~ 50

		Stream.of("홍길동", "신사임당", "세종").map(str -> str.length()).forEach(System.out::println); // 3, 4, 2 string ->
																								// integer

		// 요소의 공백을 모두 제거하고, List<String>으로 변환
		String[] wordArr = { "a b c d", "홍 길동", "hello world" };
		List<String> wordList = Arrays.stream(wordArr).map(str -> str.replaceAll(" ", "")) // 공백을 모두 제거하기위해 replaceAll
																							// 사용
				.collect(Collectors.toList());

		System.out.println("wordList = " + wordList);
	}

	/**
	 * distinct filter
	 */
	private void test2() {
		List<Integer> list = Arrays.asList(1, 2, 3, 2, 4, 3, 2, 1, 2, 4, 5);
		Stream<Integer> stream = list.stream();
		// 중복제거
		// foreach는 void 이기때문에 순서가 마지막에 있어야한다
		// 메소드체인이 가능한 이유는 리턴값이 있기 때문
		stream
				// .distinct()
				.filter(n -> n % 2 != 0).sorted().forEach(n -> System.out.println(n));

		String[] names = { "강감찬", "강원래", "홍길동", "강형욱" };

		Stream<String> stream2 = Arrays.stream(names);
		// stream2.filter(str -> str.substring(0,
		// 1).equals("강")).forEach(System.out::println);
		stream2.filter(str -> str.startsWith("강")).forEach(System.out::println);
	}

	/**
	 * stream 배열이나 컬렉션을 일관되게 제어하려는 추상화 객체
	 */
	private void test1() {
		int[] arr = { 1, 2, 3, 4, 5 };

		IntStream arrStream = Arrays.stream(arr);
		// arrStream.forEach(n -> System.out.println(n));
		// 자바스크립트의 콜백 비슷
		// 정렬
		arrStream.sorted().forEach(System.out::println);

		List<String> list = new ArrayList<>();
		list.add("홍길동");
		list.add("홍난파");
		list.add("고길동");

		Stream<String> listStream = list.stream();
		listStream.sorted().forEach(System.out::println);

		// double형 stream 생성됨, Steam이 제네릭함수이므로 변화됨
		// double -> Double
		// public static<T> Stream<T> of(T... values)
		Stream<Double> doubleSteam = Stream.of(0.1, 1.2, 3.456);
	}

}
