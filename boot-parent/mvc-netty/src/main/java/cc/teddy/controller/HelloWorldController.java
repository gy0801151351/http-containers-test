package cc.teddy.controller;

import java.time.Duration;
import java.util.Collections;
import java.util.concurrent.CopyOnWriteArraySet;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.rzd.framework.kernel.Kernel.NumberUtil;
import com.rzd.framework.kernel.Kernel.Utility;

import cc.teddy.framework.BaseController;
import reactor.core.publisher.Mono;
import reactor.core.publisher.MonoProcessor;

@RestController
//@RequestMapping("")
public class HelloWorldController extends BaseController {
	
	protected static Logger logger = LoggerFactory.getLogger(HelloWorldController.class);
	protected static CopyOnWriteArraySet<Long> mainThreadSet = new CopyOnWriteArraySet<>();
	protected static CopyOnWriteArraySet<Long> subThreadSet = new CopyOnWriteArraySet<>();

	@GetMapping("/hello")
	public String index(String sleepTime) {
		long threadId = Thread.currentThread().getId();
		mainThreadSet.add(threadId);
		long maxThreadId = Collections.max(mainThreadSet);
		int threadCount = mainThreadSet.size();
		Long lSleepTime = NumberUtil.parseLong(Utility.noEmpty(sleepTime, "0").trim());
		logger.info("Thread ID: {}\tMax Thread ID: {}\tThread Count: {}\tSleep Time: {}", threadId, maxThreadId, threadCount, lSleepTime);
		if (lSleepTime.longValue() > 0) {
			try {
				Thread.sleep(lSleepTime.longValue());
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return "hello mvc netty!";
	}
	
	@GetMapping("/hello2")
	public Mono<String> hello2(String sleepTime) {
		long mainThreadId = Thread.currentThread().getId();
		mainThreadSet.add(mainThreadId);
		long maxMainThreadId = Collections.max(mainThreadSet);
		int mainThreadCount = mainThreadSet.size();
		Long lSleepTime = NumberUtil.parseLong(Utility.noEmpty(sleepTime, "0").trim());
		logger.info("Main Thread ID: {}\tMax Thread ID: {}\tThread Count: {}", mainThreadId, maxMainThreadId, mainThreadCount);
		return MonoProcessor.<String>create()
				.onErrorReturn("error 2")
				.doOnSuccess(s -> {
			if (lSleepTime.longValue() > 0) {
				try {
					long subThreadId = Thread.currentThread().getId();
					subThreadSet.add(subThreadId);
					long maxSubThreadId = Collections.max(subThreadSet);
					int subThreadCount = subThreadSet.size();
					logger.info("Sub Thread ID: {}\t\tMax Thread ID: {}\tThread Count: {}\tSleep Time: {}", subThreadId, maxSubThreadId, subThreadCount, lSleepTime);
					Thread.sleep(lSleepTime.longValue());
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}).timeout(Duration.ofMillis(10000L)).thenReturn("hello2 mvc netty!");
//		return Mono.fromCallable(() -> {
//			if (lSleepTime.longValue() > 0) {
//				try {
//					long subThreadId = Thread.currentThread().getId();
//					subThreadSet.add(subThreadId);
//					long maxSubThreadId = Collections.max(subThreadSet);
//					int subThreadCount = subThreadSet.size();
//					logger.info("Sub Thread ID: {}\t\tMax Thread ID: {}\tThread Count: {}\tSleep Time: {}", subThreadId, maxSubThreadId, subThreadCount, lSleepTime);
//					Thread.sleep(lSleepTime.longValue());
//				} catch (Exception e) {
//					e.printStackTrace();
//				}
//			}
//			return "hello2 mvc netty!";
//		});
	}
	
//	public Mono<ServerResponse> hello3(ServerRequest request) {
//		String sleepTime = request.queryParam("sleepTime").orElseGet(() -> "0").trim();
//		long mainThreadId = Thread.currentThread().getId();
//		mainThreadSet.add(mainThreadId);
//		long maxMainThreadId = Collections.max(mainThreadSet);
//		int mainThreadCount = mainThreadSet.size();
//		Long lSleepTime = NumberUtil.parseLong(Utility.noEmpty(sleepTime, "0").trim());
//		logger.info("Main Thread ID: {}\tMax Thread ID: {}\tThread Count: {}", mainThreadId, maxMainThreadId, mainThreadCount);
//		return ServerResponse.ok().body(p -> {
//			if (lSleepTime.longValue() > 0) {
//				try {
//					long subThreadId = Thread.currentThread().getId();
//					subThreadSet.add(subThreadId);
//					long maxSubThreadId = Collections.max(subThreadSet);
//					int subThreadCount = subThreadSet.size();
//					logger.info("Sub Thread ID: {}\t\tMax Thread ID: {}\tThread Count: {}\tSleep Time: {}", subThreadId, maxSubThreadId, subThreadCount, lSleepTime);
//					Thread.sleep(lSleepTime.longValue());
//				} catch (Exception e) {
//					e.printStackTrace();
//				}
//			}
//			p.onNext("hello3 mvc netty!");
//		}, String.class);
//	}
}
