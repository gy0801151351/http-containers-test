package cc.teddy.configurator;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import reactor.core.scheduler.Scheduler;
import reactor.core.scheduler.Schedulers;

@Configuration
public class BasicConfigurator {


	@Bean(name = "controllerMethodScheduler")
	Scheduler controllerMethodScheduler() {
		int threadCount = 400;
//		Scheduler scheduler = Schedulers.newParallel("controller-parallel-thread");
		Scheduler scheduler = Schedulers.newParallel("controller-parallel-thread", threadCount, true);
//		Scheduler scheduler = Schedulers.newElastic("controller-elastic-thread", 10, true);
		for (int i = 0; i < threadCount; i++) {
			scheduler.schedule(() -> {});
		}
		return scheduler;
	}
}
