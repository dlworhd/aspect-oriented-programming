package hello.aop.proxyvs;

import hello.aop.member.MemberService;
import hello.aop.member.MemberServiceImpl;
import hello.aop.proxyvs.code.ProxyDIAspect;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;

@Slf4j
@Import(ProxyDIAspect.class)
@SpringBootTest
//@SpringBootTest("spring.aop.proxy-target-class=true")
//@SpringBootTest("spring.aop.proxy-target-class=false")
public class ProxyDITest {

	@Autowired
	MemberService memberService;

	// jdk 프록시 방식으로 프록시 객체를 생성할 때 인터페이스 기반 프록시 객체를 생성하므로, 구현체를 모름
	// 그래서 해당 타입에 프록시 객체를 주입할 수 없음
	@Autowired
	MemberServiceImpl memberServiceImpl;

	@Test
	void go(){
		log.info("memberService class = {}", memberService.getClass());
		log.info("memberServiceImple class = {}", memberServiceImpl.getClass());
		memberServiceImpl.hello("hello");
	}
}
