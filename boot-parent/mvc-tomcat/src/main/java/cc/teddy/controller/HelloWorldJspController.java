package cc.teddy.controller;

import java.util.Collections;
import java.util.concurrent.CopyOnWriteArraySet;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("")
public class HelloWorldJspController {
	
	protected static Logger logger = LoggerFactory.getLogger(HelloWorldJspController.class);
	protected static CopyOnWriteArraySet<Long> threadSet = new CopyOnWriteArraySet<>();

	@GetMapping("/helloJsp")
	public String index(@RequestParam(name="sleepTime", defaultValue="0") String sleepTime) {
		long threadId = Thread.currentThread().getId();
		threadSet.add(threadId);
		long maxThreadId = Collections.max(threadSet);
		int threadCount = threadSet.size();
		Long lSleepTime = Long.parseLong(sleepTime);
		logger.info("Thread ID: {}\tMax Thread ID: {}\tThread Count: {}\tSleep Time: {}", threadId, maxThreadId, threadCount, lSleepTime);
		if (lSleepTime.longValue() > 0) {
			try {
				Thread.sleep(lSleepTime.longValue());
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return "HelloWorld";
	}
}
