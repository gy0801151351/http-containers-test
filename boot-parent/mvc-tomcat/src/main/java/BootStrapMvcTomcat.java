import org.springframework.boot.Banner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = "cc.teddy")
public class BootStrapMvcTomcat extends SpringBootServletInitializer {

	public static void main(String[] args) throws Exception {
		SpringApplicationBuilder builder = new SpringApplicationBuilder().bannerMode(Banner.Mode.OFF);
		SpringApplication app = builder.sources(BootStrapMvcTomcat.class).build();
		app.run(args);
	}

	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
		return builder.sources(BootStrapMvcTomcat.class);
	}

}
