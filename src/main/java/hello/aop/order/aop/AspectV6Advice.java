package hello.aop.order.aop;


import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.core.annotation.Order;


@Slf4j
@Aspect
public class AspectV6Advice {


	@Around("hello.aop.order.aop.Pointcuts.orderAndService()")
	public Object doTransaction(ProceedingJoinPoint joinPoint) throws Exception {
		try {
			//@Before
			Object result = joinPoint.proceed();
			//@AfterReturning
			return result;
		} catch (Exception e) {
			//@AfterThrowing
			throw e;
		} catch (Throwable e) {
			throw new RuntimeException(e);
		} finally {
			//@After
		}
	}

	@Before("hello.aop.order.aop.Pointcuts.orderAndService()")
	public void doBefore(JoinPoint joinPoint) {
		log.info("[Before] {}", joinPoint.getSignature());
	}

	@AfterReturning(value = "hello.aop.order.aop.Pointcuts.orderAndService()", returning = "result")
	public void doAfterReturning(JoinPoint joinPoint, Object result) {
		log.info("[Return] {} Return = {}", joinPoint.getSignature(), result);
	}

	@AfterThrowing(value = "hello.aop.order.aop.Pointcuts.orderAndService()", throwing = "ex")
	public void doAfterThrowing(JoinPoint joinPoint, Exception ex) {
		log.info("[Exception] {} Message = {}", joinPoint.getSignature(), ex.getMessage());

	}

	@After("hello.aop.order.aop.Pointcuts.orderAndService()")
	public void doAfter(JoinPoint joinPoint) {
		log.info("[After] {}", joinPoint.getSignature());
	}

}
