package com.example.ai_agent_service.controller;

import com.example.ai_agent_service.dto.AgentRequest;
import com.example.ai_agent_service.dto.AgentResponse;
import com.example.ai_agent_service.service.AiAgentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestClient;

@RestController
@RequestMapping("/agent")
public class AiAgentController {

    @Autowired
    private AiAgentService aiAgentService;
    
    @Autowired
    private RestClient.Builder restClientBuilder;
    
//    @Value("${wrapper.service.url}")
//    private String wrapperServiceUrl;

    @PostMapping("/invoke")
    public AgentResponse invoke(@RequestBody AgentRequest request) {
        return aiAgentService.process(request.getInput());
    }

//    @GetMapping("/metrics")
//    public Object getMetrics() {
//        try {
//            String metricsUrl = wrapperServiceUrl.replace("/ai/chat", "/metrics");
//            return restClientBuilder.build()
//                    .get()
//                    .uri(metricsUrl)
//                    .retrieve()
//                    .body(String.class);
//        } catch (Exception e) {
//            return "{\"error\": \"Metrics endpoint not available: " + e.getMessage() + "\"}";
//        }
//    }
}
