package com.barshop.app;

import org.apache.log4j.Logger;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;

import com.google.common.base.Predicates;

import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.Tag;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@SpringBootApplication
@Configuration
@EnableSwagger2
@EnableScheduling
public class App {

    public static final String COUNTRY_TAG = "Country";

    private static final Logger LOGGER = Logger.getLogger(App.class.getName());
    
    public static void main( String[] args ) {
        LOGGER.info("engine: " +System.getenv("ENGINE_DB"));
        LOGGER.info("conectordb: " +System.getenv("CONNECTOR_DB"));
        LOGGER.info("dbdomain: " +System.getenv("DB_DOMAIN"));
        LOGGER.info("engine: " +System.getenv("ENGINE_DB"));
        LOGGER.info("loglvl: " +System.getenv("HB_LOG_LVL"));
        
        SpringApplication.run(App.class, args);
    }

    @Bean
    public Docket api() {
        return new Docket(DocumentationType.SWAGGER_2).select().apis(RequestHandlerSelectors.any()).paths(Predicates.not(PathSelectors.regex("/error.*"))).build().enableUrlTemplating(false)
                .tags(new Tag(COUNTRY_TAG, "Everything about countries."));
    }

}
