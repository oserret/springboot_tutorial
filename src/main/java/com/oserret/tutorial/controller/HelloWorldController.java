package com.oserret.tutorial.controller;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin
public class HelloWorldController {

	@RequestMapping({ "/documents/hello" })
	public String hello() {
		return "Hello World";
	}

}