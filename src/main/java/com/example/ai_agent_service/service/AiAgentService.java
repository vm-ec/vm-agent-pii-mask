package com.example.ai_agent_service.service;

import com.example.ai_agent_service.dto.AgentResponse;
import com.example.ai_agent_service.dto.WrapperRequest;
import tools.jackson.databind.JsonNode;
import tools.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

@Service
public class AiAgentService {

    private final RestClient restClient;
    private final String wrapperServiceUrl;
    private final ObjectMapper objectMapper;

    public AiAgentService(RestClient.Builder restClientBuilder,
                          @Value("${wrapper.service.url}") String wrapperServiceUrl,
                          ObjectMapper objectMapper) {
        this.restClient = restClientBuilder.build();
        this.wrapperServiceUrl = wrapperServiceUrl;
        this.objectMapper = objectMapper;
    }

    public AgentResponse process(String input) {
        WrapperRequest request = new WrapperRequest(input);
        
        String responseStr = restClient.post()
                .uri(wrapperServiceUrl)
                .contentType(MediaType.APPLICATION_JSON)
                .body(request)
                .retrieve()
                .body(String.class);

        try {
            JsonNode jsonNode = objectMapper.readTree(responseStr);
            String output = jsonNode.get("output").asText();
            
            // Check if it's an error message
            if (output.startsWith("ERROR:")) {
                output = "I'm currently experiencing high demand. Please try again in a moment.";
            } else {
                output = output.replaceAll("\\*\\*(.*?)\\*\\*", "$1");
            }
            
            AgentResponse response = new AgentResponse();
            response.setOutput(output);
            return response;
        } catch (Exception e) {
            AgentResponse response = new AgentResponse();
            response.setOutput("Sorry, I'm having trouble processing your request. Please try again.");
            return response;
        }
    }
}
