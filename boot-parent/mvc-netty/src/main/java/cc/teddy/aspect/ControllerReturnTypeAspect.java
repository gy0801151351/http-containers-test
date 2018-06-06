package cc.teddy.aspect;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.Order;
import org.springframework.jmx.access.InvocationFailureException;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Scheduler;

@Aspect
public class ControllerReturnTypeAspect {
	
	protected static Logger logger = LoggerFactory.getLogger(ControllerReturnTypeAspect.class);
	
	private Scheduler controllerMethodScheduler;

	public ControllerReturnTypeAspect(Scheduler controllerMethodScheduler) {
		super();
		this.controllerMethodScheduler = controllerMethodScheduler;
	}

	@Order(1)
	@Around("execution(public * cc.teddy.controller.*Controller.*(..))")
	public Object proceed(ProceedingJoinPoint pjp) throws Throwable {
		Object returnVal = null;
		try {
			Signature signature = pjp.getSignature();
			if (signature instanceof MethodSignature) {
				MethodSignature msig = (MethodSignature) signature;
				Class<?> returnTypeClass = msig.getReturnType();
				if (Mono.class.isAssignableFrom(returnTypeClass)) {
					Mono<?> returnMono = Mono.defer(() -> {
						try {
							Mono<?> mono = (Mono<?>) pjp.proceed();
							mono = mono == null ? Mono.empty() : mono;
							return mono;
						} catch (Throwable t) {
							return Mono.error(t);
						}
					});
					returnVal = returnMono.subscribeOn(controllerMethodScheduler);
				} else if (Flux.class.isAssignableFrom(returnTypeClass)) {
					Flux<?> returnFlux = Flux.defer(() -> {
						try {
							Flux<?> flux = (Flux<?>) pjp.proceed();
							flux = flux == null ? Flux.empty() : flux;
							return flux;
						} catch (Throwable t) {
							return Flux.error(t);
						}
					});
					returnVal = returnFlux.subscribeOn(controllerMethodScheduler);
				} else {
					throw new InvocationFailureException(String.format("The return type of method [%s] in class [%s] is not supported! Method return type must be [%s] or [%s]", msig.getMethod().toString(), pjp.getTarget().getClass().getName(), Mono.class.getName(), Flux.class.getName()));
				}
			}
			return returnVal;
		} catch (Throwable t) {
			logger.error(t.getMessage(), t);
			throw t;
		}
	}

	
}
