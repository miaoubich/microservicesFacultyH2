//package com.miaoubich.swagger.api.config;
//
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//
//import springfox.documentation.builders.ApiInfoBuilder;
//import springfox.documentation.builders.PathSelectors;
//import springfox.documentation.builders.RequestHandlerSelectors;
//import springfox.documentation.service.ApiInfo;
//import springfox.documentation.spi.DocumentationType;
//import springfox.documentation.spring.web.plugins.Docket;
//import springfox.documentation.swagger2.annotations.EnableSwagger2;
////import static springfox.documentation.builders.PathSelectors.regex;
//
//@Configuration
//@EnableSwagger2
//public class GradeConfig {
//	
//	@Bean
//	public Docket swaggerConfiguration() {
//		//return a prepared Docket instance
//		return new Docket(DocumentationType.SWAGGER_2)
//				.groupName("Java miaoubich")
////				.apiInfo(apiInfo())
//				.select()
//				.paths(PathSelectors.any())
////				.paths(regex("/api/grade/*"))
//				.apis(RequestHandlerSelectors.basePackage("com.miaoubich"))
//				.build();
//	}
//
//	private ApiInfo apiInfo() {
//		return new ApiInfoBuilder().title("Grade Service")
//				.description("Sample Description")
//				.build();
//	}
//
//}
