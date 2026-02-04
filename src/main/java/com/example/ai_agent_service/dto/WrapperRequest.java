package com.example.ai_agent_service.dto;

public class WrapperRequest {
    private String input;

    public WrapperRequest() {}  // needed for deserialization

    public WrapperRequest(String input) {
        this.input = input;
    }

    public String getInput() { return input; }
    public void setInput(String input) { this.input = input; }
}
