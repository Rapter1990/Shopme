package com.shopme.admin.config;

import java.nio.file.Path;
import java.nio.file.Paths;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class MvcConfig implements WebMvcConfigurer {

	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
		
		// User
		exposeDirectory("user-photos", registry);
		
		// Category		
		exposeDirectory("../category-images", registry);
		
		// Brand		
		exposeDirectory("../brand-logos", registry);
		
		// Product
		exposeDirectory("../product-images", registry);
		
		// Site Logo
		exposeDirectory("../site-logo", registry);
		
	}

	private void exposeDirectory(String pathPattern, ResourceHandlerRegistry registry) {
		
		Path path = Paths.get(pathPattern);
		String absolutePath = path.toFile().getAbsolutePath();

		String logicalPath = pathPattern.replace("../", "") + "/**";
		
		registry.addResourceHandler(logicalPath)
			.addResourceLocations("file:/" + absolutePath + "/");
	}
}
