package hello.aop.order.aop;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;

@Slf4j
@Aspect
public class AspectV1 {



	// Joinpoint는 추가 동작이 삽입될 수 있는 프로그램 실행의 특정 지점
	@Around("execution(* hello.aop.order..*(..))")
	public Object doLog(ProceedingJoinPoint joinPoint) throws Throwable {
		// 메서드의 시그니처 로깅
		log.info("[log] {}", joinPoint.getSignature());
		return joinPoint.proceed();
	}
}
