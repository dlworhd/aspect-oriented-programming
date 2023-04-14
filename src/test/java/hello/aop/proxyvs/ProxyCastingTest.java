package hello.aop.proxyvs;

import hello.aop.member.MemberService;
import hello.aop.member.MemberServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.aop.framework.ProxyFactory;
import org.springframework.boot.test.context.SpringBootTest;

@Slf4j
public class ProxyCastingTest {

	@Test
	void jdkProxy(){
		MemberServiceImpl target = new MemberServiceImpl();
		ProxyFactory proxyFactory = new ProxyFactory(target);
		proxyFactory.setProxyTargetClass(false); // jdk 방식의 proxy 적용

		// memberServiceProxy = jdk 프록시 -> 인터페이스 기반으로 프록시 객체가 생성되는데,
		// 인터페이스 타입의 프록시 객체는 구체 타입를 모르는 상태이기 때문에, 캐스팅 시에 에러가 발생한다.
		MemberService memberServiceProxy = (MemberService) proxyFactory.getProxy();
		log.info("proxy class = {}", memberServiceProxy.getClass());

		Assertions.assertThrows(ClassCastException.class, () -> {
			MemberServiceImpl castingMemberService = (MemberServiceImpl) memberServiceProxy; }
		);
	}

	@Test
	void cglibProxy(){
		MemberServiceImpl target = new MemberServiceImpl();
		ProxyFactory proxyFactory = new ProxyFactory(target);
		proxyFactory.setProxyTargetClass(true); // jdk 방식의 proxy 적용

		// memberServiceProxy = cglib 프록시 -> 구체 클래스 기반의 프록시 객체가 생성되므로,
		// 구체 클래스는 물론 인터페이스로도 형변환이 가능하다.
		MemberServiceImpl memberServiceProxy1 = (MemberServiceImpl) proxyFactory.getProxy();
		MemberService memberServiceProxy2 = (MemberService) proxyFactory.getProxy();
	}
}
