package hello.aop.pointcut;

import hello.aop.member.MemberServiceImpl;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.aop.aspectj.AspectJExpressionPointcut;

import java.lang.reflect.Method;

import static org.assertj.core.api.Assertions.*;

public class ArgsTest {

	Method helloMethod;

	@BeforeEach
	void init() throws NoSuchMethodException {
		helloMethod = MemberServiceImpl.class.getMethod("hello", String.class);
	}

	private AspectJExpressionPointcut pointcut(String expression) {
		AspectJExpressionPointcut pointcut = new AspectJExpressionPointcut();
		pointcut.setExpression(expression);
		return pointcut;
	}


	/**
	 * args는 매개 변수 타입의 부모도 허용하지만, execution의 경우는 허용하지 않는다.
	 * execution은 선언된 타입의 정보를 가지고 판단하지만, args는 동적으로 파라미터에 값이 넘어오기 때문에 실제 들어온 인스턴스를 보고 판단한다.
	 */


	@Test
	void args() {
		assertThat(pointcut("args(String)").matches(helloMethod, MemberServiceImpl.class)).isTrue();
		assertThat(pointcut("args(Object)").matches(helloMethod, MemberServiceImpl.class)).isTrue();
		assertThat(pointcut("args()").matches(helloMethod, MemberServiceImpl.class)).isFalse();
		assertThat(pointcut("args(..)").matches(helloMethod, MemberServiceImpl.class)).isTrue();
		assertThat(pointcut("args(*)").matches(helloMethod, MemberServiceImpl.class)).isTrue();
		assertThat(pointcut("args(String, ..)").matches(helloMethod, MemberServiceImpl.class)).isTrue();
	}

	@Test
	void argsVsExecution() {
		assertThat(pointcut("execution(* *(String))").matches(helloMethod, MemberServiceImpl.class)).isTrue();
		assertThat(pointcut("execution(* *(java.io.Serializable))").matches(helloMethod, MemberServiceImpl.class)).isFalse();
		assertThat(pointcut("execution(* *(Object))").matches(helloMethod, MemberServiceImpl.class)).isFalse();
	}
}
