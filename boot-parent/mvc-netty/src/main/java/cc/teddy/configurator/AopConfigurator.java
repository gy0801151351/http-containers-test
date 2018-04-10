package cc.teddy.configurator;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import cc.teddy.aspect.ControllerReturnTypeAspect;
import reactor.core.scheduler.Scheduler;

@Configuration
public class AopConfigurator {

	@Bean
	ControllerReturnTypeAspect controllerReturnTypeAspect(Scheduler controllerMethodScheduler) {
		return new ControllerReturnTypeAspect(controllerMethodScheduler);
	}
}
