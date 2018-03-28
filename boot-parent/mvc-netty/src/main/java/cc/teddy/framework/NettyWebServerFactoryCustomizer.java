package cc.teddy.framework;

import org.springframework.boot.autoconfigure.web.ServerProperties;
import org.springframework.boot.autoconfigure.web.reactive.ReactiveWebServerFactoryCustomizer;
import org.springframework.boot.web.reactive.server.ConfigurableReactiveWebServerFactory;
import org.springframework.stereotype.Component;

@Component
public class NettyWebServerFactoryCustomizer extends ReactiveWebServerFactoryCustomizer {

	public NettyWebServerFactoryCustomizer(ServerProperties serverProperties) {
		super(serverProperties);
	}

	@Override
	public void customize(ConfigurableReactiveWebServerFactory factory) {
		super.customize(factory);
//		if (factory instanceof NettyReactiveWebServerFactory) {
//			NettyReactiveWebServerFactory nettyFactory = (NettyReactiveWebServerFactory) factory;
//		}
	}

	
}
