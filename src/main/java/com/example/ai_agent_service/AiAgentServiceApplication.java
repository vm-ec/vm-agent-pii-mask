package com.example.ai_agent_service;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestClient;

@SpringBootApplication(scanBasePackages = {"com.example.ai_agent_service", "com.vm.chat"})
public class AiAgentServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(AiAgentServiceApplication.class, args);
	}

	@Bean
	public RestClient.Builder restClientBuilder() {
		return RestClient.builder();
	}

}
