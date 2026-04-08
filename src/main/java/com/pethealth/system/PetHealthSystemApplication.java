package com.pethealth.system;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@SpringBootApplication
public class PetHealthSystemApplication {

	public static void main(String[] args) {
		SpringApplication.run(PetHealthSystemApplication.class, args);
	}

	@Configuration
	public class WebConfig implements WebMvcConfigurer {
		@Override
		public void addResourceHandlers(ResourceHandlerRegistry registry) {
			// 配置前端静态资源路径
			registry.addResourceHandler("/**")
					.addResourceLocations("file:frontend/");
		}
	}

}
