package hello.aop.pointcut;

import hello.aop.member.MemberService;
import hello.aop.member.MemberServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.aop.aspectj.AspectJExpressionPointcut;

import java.lang.reflect.Method;

import static org.assertj.core.api.Assertions.*;

@Slf4j
public class ExecutionTest {


	/**
	 * AspectJExpressionPointcut = 포인트컷 표현식을 처리해주는 객체
	 */
	AspectJExpressionPointcut pointcut = new AspectJExpressionPointcut();
	Method helloMethod;

	@BeforeEach
	void init() throws NoSuchMethodException {
		helloMethod = MemberServiceImpl.class.getMethod("hello", String.class);
	}

	@Test
	void printMethod(){
		log.info("helloMethod() = {}", helloMethod);
	}

	/**
	 * execution 문법 = 접근제어자? 반환타입 선언타입?메서드이름(파라미터) 예외?)
	 * ?는 생략 가능하고, 선언타입은 해당 메서드가 존재하는 클래스를 말한다.
	 * 파라미터는 ..처럼 사용할 수 있는데 어떤 타입, 몇 개든 상관 없다는 뜻이다.
	 * execution(public String hello.aop.member.MemberServiceImpl.hello(String))를 예시로 들면,
	 * 접근제어자 = public(생략 가능)
	 * 반환타입 = String
	 * 선언타입 = hello.aop.member.MemberServiceImpl(생략 가능)
	 * 메서드명 = hello()
	 * 파라미터 = String
	 * 예외 = (생략 가능)
	 * pointcut.matches(메서드, 클래스)는 해당 클래스에 메서드가 pointcut의 표현식과 매칭이 되는지 확인하는 메서드
	 */
	@Test
	void exactMatch(){
		pointcut.setExpression("execution(public String hello.aop.member.MemberServiceImpl.hello(String))");
		assertThat(pointcut.matches(helloMethod, MemberServiceImpl.class)).isTrue();
	}

	@Test
	void allMatch(){
		// 접근 제어자는 생략되었고, 맨 앞에 오는 *은 반환 타입, 선언 타입은 생략되었고, *은 모든 메서드이름, ..는 매개 변수가 몇 개든, 어떤 타입이든 상관 없다는 뜻
		pointcut.setExpression("execution(* *(..))");
		assertThat(pointcut.matches(helloMethod, MemberServiceImpl.class)).isTrue();
	}

	@Test
	void nameMatch(){
		// 접근 제어자는 생략되었고, 반환 타입은 *, 선언 타입은 생략, hello라는 메서드 이름을 가지고, 모든 매개 변수, 몇 개든 상관 없이 가능
		pointcut.setExpression("execution(* hello(..))");
		assertThat(pointcut.matches(helloMethod, MemberServiceImpl.class)).isTrue();
	}

	@Test
	void nameMatchStar1(){
		// 접근 제어자는 생략되었고, 반환 타입은 *, 선언 타입은 생략, hel로 시작하는 어떤 메서드 이름이든 상관 없고, 모든 매개 변수, 몇 개든 상관 없이 가능
		pointcut.setExpression("execution(* hel*(..))");
		assertThat(pointcut.matches(helloMethod, MemberServiceImpl.class)).isTrue();
	}

	@Test
	void nameMatchStar2(){
		// 접근 제어자는 생략되었고, 반환 타입은 *, 선언 타입은 생략, el이 들어가는 어떤 메서드 이름이든 상관 없고, 모든 매개 변수, 몇 개든 상관 없이 가능
		pointcut.setExpression("execution(* *el*(..))");
		assertThat(pointcut.matches(helloMethod, MemberServiceImpl.class)).isTrue();
	}

	@Test
	void nameMatchFalse() {
		// 접근 제어자는 생략되었고, 반환 타입은 *, 선언 타입은 생략, nono로 시작하는 메서드 이름, 모든 매개 변수, 몇 개든 상관 없이 가능
		pointcut.setExpression("execution(* nono(..))");
		assertThat(pointcut.matches(helloMethod, MemberServiceImpl.class)).isFalse();
	}

	@Test
	void packageExactMatch1(){
		pointcut.setExpression("execution(* hello.aop.member.MemberServiceImpl.hello(..))");
		assertThat(pointcut.matches(helloMethod, MemberServiceImpl.class)).isTrue();
	}

	@Test
	void packageExactMatch2(){
		pointcut.setExpression("execution(* hello.aop.member.*.*(..))");
		assertThat(pointcut.matches(helloMethod, MemberServiceImpl.class)).isTrue();
	}


	/**
	 * False가 나오는 이유는 패키지는 .과 ..을 사용하는데,
	 * .을 사용하는 경우에는 정확히 딱 패키지가 나와야 하고, ..을 사용하는 경우에는 해당 패키지를 포함한 하위 패키지까지 적용시킨다.
	 * 즉, *이라는 패키지가 있는 게 아니기 때문에, ..*을 써서 해당 패키지 및 하위 패키지의 모든 패키지를 포함하도록 해야 한다.
	 */
	@Test
	void packageExactMatchFalse(){
		pointcut.setExpression("execution(* hello.aop.*.*(..))");
		assertThat(pointcut.matches(helloMethod, MemberServiceImpl.class)).isFalse();
	}

	@Test
	void packageMatchSubPackage1(){
		// ..은 member 패키지도 포함하기 때문에 상관 없다.
		pointcut.setExpression("execution(* hello.aop.member..*.*(..))");
		assertThat(pointcut.matches(helloMethod, MemberServiceImpl.class)).isTrue();
	}

	@Test
	void packageMatchSubPackage2(){
		pointcut.setExpression("execution(* hello.aop..*.*(..))");
		assertThat(pointcut.matches(helloMethod, MemberServiceImpl.class)).isTrue();
	}

	@Test
	void typeExactMatch(){
		pointcut.setExpression("execution(* hello.aop.member.MemberServiceImpl.*(..))");
		assertThat(pointcut.matches(helloMethod, MemberServiceImpl.class)).isTrue();
	}

	@Test
	void typeMatchSuperType(){
		pointcut.setExpression("execution(* hello.aop.member.MemberService.*(..))");
		assertThat(pointcut.matches(helloMethod, MemberServiceImpl.class)).isTrue();
	}

	@Test
	void typeMatchInternal1() throws NoSuchMethodException {
		pointcut.setExpression("execution(* hello.aop.member.MemberServiceImpl.*(..))");
		Method method = MemberServiceImpl.class.getMethod("internal", String.class);
		assertThat(pointcut.matches(method, MemberServiceImpl.class)).isTrue();
	}

	@Test
	void typeMatchInternal2() throws NoSuchMethodException {
		pointcut.setExpression("execution(* hello.aop.member.MemberService.*(..))");
		Method method = MemberServiceImpl.class.getMethod("internal", String.class);
		assertThat(pointcut.matches(method, MemberServiceImpl.class)).isFalse();
	}

	@Test
	void argsMatch(){
		pointcut.setExpression("execution(* *(String))");
		assertThat(pointcut.matches(helloMethod, MemberServiceImpl.class)).isTrue();
	}
	@Test
	void argsMatchNoArgs(){
		pointcut.setExpression("execution(* *())");
		assertThat(pointcut.matches(helloMethod, MemberServiceImpl.class)).isFalse();
	}

	@Test
	void argsMatchStar(){
		pointcut.setExpression("execution(* *(*))");
		assertThat(pointcut.matches(helloMethod, MemberServiceImpl.class)).isTrue();
	}

	@Test
	void argsMatchAll(){
		pointcut.setExpression("execution(* *(..))");
		assertThat(pointcut.matches(helloMethod, MemberServiceImpl.class)).isTrue();
	}

	@Test
	void argsMatchComplex(){
		// String 하나만 와도 됨
		pointcut.setExpression("execution(* *(String, ..))");
		assertThat(pointcut.matches(helloMethod, MemberServiceImpl.class)).isTrue();
	}


}
