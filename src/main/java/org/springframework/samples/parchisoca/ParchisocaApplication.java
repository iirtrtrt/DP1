package org.springframework.samples.parchisoca;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.context.TypeExcludeFilter;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;


@SpringBootApplication
public class ParchisocaApplication{



	public static void main(String[] args) {
		SpringApplication.run(ParchisocaApplication.class);
	}

}


