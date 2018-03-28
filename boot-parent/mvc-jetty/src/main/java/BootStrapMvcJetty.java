import org.springframework.boot.Banner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages="cc.teddy")
public class BootStrapMvcJetty {

	public static void main(String[] args) throws Exception {
		SpringApplicationBuilder builder = new SpringApplicationBuilder().bannerMode(Banner.Mode.OFF);
		SpringApplication app = builder.sources(BootStrapMvcJetty.class).build();
		app.run(args);
	}

}
