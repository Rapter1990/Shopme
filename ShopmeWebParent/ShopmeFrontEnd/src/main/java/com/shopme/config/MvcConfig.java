package com.shopme.config;

import java.nio.file.Path;
import java.nio.file.Paths;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Description;
import org.springframework.web.servlet.ViewResolver;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.thymeleaf.spring5.SpringTemplateEngine;
import org.thymeleaf.spring5.view.ThymeleafViewResolver;
import org.thymeleaf.templateresolver.ClassLoaderTemplateResolver;

@Configuration
public class MvcConfig implements WebMvcConfigurer{

//	@Override
//	public void addResourceHandlers(ResourceHandlerRegistry registry) {
//		exposeDirectory("../category-images", registry);
//		exposeDirectory("../brand-logos", registry);
//		exposeDirectory("../product-images", registry);
//		exposeDirectory("../site-logo", registry);
//	}
//
//	private void exposeDirectory(String pathPattern, ResourceHandlerRegistry registry) {
//		Path path = Paths.get(pathPattern);
//		String absolutePath = path.toFile().getAbsolutePath();
//
//		String logicalPath = pathPattern.replace("../", "") + "/**";
//
//		registry.addResourceHandler(logicalPath)
//			.addResourceLocations("file:/" + absolutePath + "/");		
//	}
	
	/*
	 * @Bean
	 * 
	 * @Description("Thymeleaf template engine with Spring integration") public
	 * SpringTemplateEngine templateEngine() {
	 * 
	 * var templateEngine = new SpringTemplateEngine();
	 * templateEngine.setTemplateResolver(templateResolver());
	 * 
	 * return templateEngine; }
	 * 
	 * @Bean
	 * 
	 * @Description("Thymeleaf view resolver") public ViewResolver viewResolver() {
	 * 
	 * var viewResolver = new ThymeleafViewResolver();
	 * 
	 * viewResolver.setTemplateEngine(templateEngine());
	 * viewResolver.setCharacterEncoding("UTF-8");
	 * 
	 * return viewResolver; }
	 * 
	 * @Bean
	 * 
	 * @Description("Thymeleaf template resolver serving HTML 5") public
	 * ClassLoaderTemplateResolver templateResolver() {
	 * 
	 * var templateResolver = new ClassLoaderTemplateResolver();
	 * 
	 * templateResolver.setPrefix("templates/");
	 * templateResolver.setCacheable(false); templateResolver.setSuffix(".html");
	 * templateResolver.setTemplateMode("HTML5");
	 * templateResolver.setCharacterEncoding("UTF-8");
	 * 
	 * return templateResolver; }
	 */
}
