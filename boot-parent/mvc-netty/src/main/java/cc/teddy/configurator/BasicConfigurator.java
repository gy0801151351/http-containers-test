package cc.teddy.configurator;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import reactor.core.scheduler.Scheduler;
import reactor.core.scheduler.Schedulers;

@Configuration
public class BasicConfigurator {

	@Bean(name = "controllerMethodScheduler")
	Scheduler controllerMethodScheduler() {
		Scheduler scheduler = Schedulers.newParallel("controller-parallel-thread", 120, true);
//		Scheduler scheduler = Schedulers.newElastic("controller-elastic-thread", 200, true);
		return scheduler;
	}
}
