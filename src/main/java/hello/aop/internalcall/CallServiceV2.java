package hello.aop.internalcall;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class CallServiceV2 {

	// ObjectProvider는 객체에 대한 조회를 빈 생성 시점이 아니라 실제 사용하는 시점으로 지연할 수 있게 도와준다.
	// 즉, getObject()를 할 때 조회를 함 -> Proxy 객체로 만들어진 이후에 빈 저장소에 저장된 Proxy 객체를 가져와서, Proxy 객체로 internal() 호출
	private final ObjectProvider<CallServiceV2> objectProvider;

	public void external(){
		log.info("call external");
		CallServiceV2 callServiceV2 = objectProvider.getObject();
		callServiceV2.internal();
	}
	public void internal(){
		log.info("call internal");
	}

}
