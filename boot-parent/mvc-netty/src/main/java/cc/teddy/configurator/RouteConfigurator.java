package cc.teddy.configurator;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RouteConfigurator {
	
	@Value("${server.servlet.context-path}")
	private String baseContext;

//	@Bean
//	public RouterFunction<ServerResponse> monoRouterFunction(HelloWorldController helloWorldController) {
//		return RouterFunctions.route(RequestPredicates.GET(baseContext + "/hello3").and(RequestPredicates.accept(MediaType.APPLICATION_JSON, MediaType.APPLICATION_FORM_URLENCODED)), helloWorldController::hello3);
//	}
}
