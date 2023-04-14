	package hello.aop.internalcall;


	import hello.aop.internalcall.aop.CallLogAspect;
	import org.junit.jupiter.api.Test;
	import org.springframework.beans.factory.annotation.Autowired;
	import org.springframework.boot.test.context.SpringBootTest;
	import org.springframework.context.annotation.Import;

	@Import(CallLogAspect.class)
	@SpringBootTest("spring.main.allow-circular-references=true") // 스프링 2.6부터 순환 참조 금지됨
	class CallServiceV1Test {

		@Autowired
		CallServiceV1 callServiceV1;

		@Test
		void external(){
			callServiceV1.external();
		}

	}