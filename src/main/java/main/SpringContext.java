package main;

import java.io.IOException;

import org.springframework.context.annotation.Bean;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;

public class SpringContext {
	
	@Bean(name = "login")
    public WorkPage createLogin() throws IOException {
        return new WorkPage();
    }
 
 @Bean
    public static PropertySourcesPlaceholderConfigurer setUp() {
        return new PropertySourcesPlaceholderConfigurer();
    }
}
