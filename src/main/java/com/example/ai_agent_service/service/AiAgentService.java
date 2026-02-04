package com.example.ai_agent_service.service;

import com.example.ai_agent_service.dto.AgentResponse;
import com.example.ai_agent_service.dto.WrapperRequest;
import tools.jackson.databind.JsonNode;
import tools.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class AiAgentService {

    private static final Logger logger = LoggerFactory.getLogger(AiAgentService.class);
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

    public AgentResponse process(String input, String model) {
        WrapperRequest request = new WrapperRequest(input, model);
        
        try {
            logger.info("Making request to wrapper service: {}", wrapperServiceUrl);
            String responseStr = restClient.post()
                    .uri(wrapperServiceUrl)
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(request)
                    .retrieve()
                    .body(String.class);

            try {
                logger.info("Response from wrapper service: {}", responseStr);
                JsonNode jsonNode = objectMapper.readTree(responseStr);
                
                // Try different possible response structures
                String output = null;
                
                if (jsonNode.has("output") && !jsonNode.get("output").isNull()) {
                    output = jsonNode.get("output").asText();
                } else if (jsonNode.has("response") && !jsonNode.get("response").isNull()) {
                    output = jsonNode.get("response").asText();
                } else if (jsonNode.has("message") && !jsonNode.get("message").isNull()) {
                    output = jsonNode.get("message").asText();
                } else if (jsonNode.isTextual()) {
                    output = jsonNode.asText();
                } else {
                    logger.error("Could not find output field. Response structure: {}", jsonNode.toPrettyString());
                    output = "Received unexpected response format.";
                }
                
                // Remove markdown bold formatting
                output = output.replaceAll("\\*\\*(.*?)\\*\\*", "$1");
                
                AgentResponse response = new AgentResponse();
                response.setOutput(output);
                return response;
            } catch (Exception e) {
                logger.error("Error parsing response from wrapper service", e);
                AgentResponse response = new AgentResponse();
                response.setOutput("Sorry, I'm having trouble processing your request. Please try again.");
                return response;
            }
        } catch (RestClientException e) {
            logger.error("Error calling wrapper service at {}: {}", wrapperServiceUrl, e.getMessage());
            AgentResponse response = new AgentResponse();
            response.setOutput("The AI service is currently unavailable. Please try again later.");
            return response;
        }
    }
    // Keep backward compatibility
    public AgentResponse process(String input) {
        return process(input, "openai");
    }
}
