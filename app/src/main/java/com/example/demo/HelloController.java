package com.example.demo;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloController {

	@Autowired
	AmazonClient awsClient;

	@GetMapping("/")
	public String index() {
		return "Greetings from Spring Boot!";
	}

	@GetMapping(value = "/queues", produces="application/json")
	public List<String> getQueues() {
		return awsClient.listQueues();
	}

	@GetMapping(value = "/sendMessage")
	public String sendMessage() {
		awsClient.sendMessage("Hello from Spring Boot!");
		return "Message sent!";
	}
}

