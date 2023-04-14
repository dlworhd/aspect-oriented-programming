package hello.aop.pointcut;

import hello.aop.member.MemberService;
import hello.aop.member.annotation.ClassAop;
import hello.aop.member.annotation.MethodAop;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;

@Slf4j
@Import({ParameterTest.ParameterAspect.class})
@SpringBootTest
public class ParameterTest {

	@Autowired
	MemberService memberService;

	@Test
	void success(){
		log.info("memberService Proxy = {}", memberService.getClass());
		memberService.hello("helloA");
	}


	@Aspect
	static class ParameterAspect{

		@Pointcut("execution(* hello.aop.member..*(..))")
		public void allMember(){}

		@Around("allMember()")
		public Object logArgs1(ProceedingJoinPoint joinPoint) throws Throwable {
			// Pointcut으로 잡힌 target의 hello 메서드를 실행하면서 넘어온 인자 값들을 Joinpoint를 통해서 알아낼 수 있다.
			Object arg1 = joinPoint.getArgs()[0];
			log.info("[logArgs1] {}, args = {}", joinPoint.getSignature(), arg1);
			return joinPoint.proceed();
		}

		@Around("allMember() && args(arg, ..)")
		public Object logArgs2(ProceedingJoinPoint joinPoint, Object arg) throws Throwable {
			// Pointcut으로 잡힌 target의 hello 메서드를 실행하면서 넘어온 인자 값들을 Joinpoint를 사용하지 않고, args를 사용해서 알아낼 수 있다.
			log.info("[logArgs2] {}, args = {}", joinPoint.getSignature(), arg);
			return joinPoint.proceed();
		}

		@Before("allMember() && args(arg, ..)")
		public void logArgs3(Object arg) {
			// Pointcut으로 잡힌 target의 hello 메서드를 실행하면서 넘어온 인자 값들을 Joinpoint를 사용하지 않고, args를 사용해서 알아낼 수 있다.
			log.info("[logArgs3] args = {}", arg);
		}

		@Before("allMember() && this(obj)")
		public void thisArgs(JoinPoint joinPoint, MemberService obj) {
			// this는 프록시 객체를 전달받는다. 사용하기 위해서는 obj처럼 이름을 설정하고, 파라미터에 넣어서 사용하면 된다.
			log.info("[this] {}, obj = {}", joinPoint.getSignature(), obj.getClass());
		}

		@Before("allMember() && target(obj)")
		public void targetArgs(JoinPoint joinPoint, MemberService obj) {
			// target은 실제 대상 객체(MemberService의 구현체도 받을 수 있는 것)를 받는다.
			log.info("[target] {}, obj = {}", joinPoint.getSignature(), obj.getClass());
		}

		@Before("allMember() && @target(annotation)")
		public void atTarget(JoinPoint joinPoint, ClassAop annotation) {
			// @target, @within은 타입의 annotation을 직접 받는다.
			log.info("[@target] {}, obj = {}", joinPoint.getSignature(), annotation);
		}

		@Before("allMember() && @within(annotation)")
		public void atWithin(JoinPoint joinPoint, ClassAop annotation) {
			// @target, @within은 타입의 annotation을 직접 받는다.
			log.info("[@within] {}, obj = {}", joinPoint.getSignature(), annotation);
		}

		@Before("allMember() && @annotation(annotation)")
		public void atAnnotation(JoinPoint joinPoint, MethodAop annotation) {
			// @annotation은 메서드에 달려있는 annotation을 전달받는다. 테스트에서는 @MethodAop("test Value")를 가져옴
			log.info("[@annotation] {}, annotationValue = {}", joinPoint.getSignature(), annotation.value());
		}
	}
}
