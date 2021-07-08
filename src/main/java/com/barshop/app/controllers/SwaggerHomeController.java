package com.barshop.app.controllers;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import springfox.documentation.annotations.ApiIgnore;

@Controller
@RequestMapping("")
@ApiIgnore
public class SwaggerHomeController {

	private static final Logger LOGGER = Logger.getLogger(SwaggerHomeController.class.getName());

	@GetMapping(path = "")
	public String swaggerUI() {
		LOGGER.info("Redirect swagger home");
		return "redirect:/swagger-ui.html";
	}
}
