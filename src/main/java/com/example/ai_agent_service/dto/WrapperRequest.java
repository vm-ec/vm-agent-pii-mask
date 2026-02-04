package com.example.ai_agent_service.dto;

public class WrapperRequest {
    private String input;
    private String model = "openai"; // default value

    public WrapperRequest() {}  // needed for deserialization

    public WrapperRequest(String input) {
        this.input = input;
    }

    public WrapperRequest(String input, String model) {
        this.input = input;
        this.model = model ;
    }

    public String getInput() { return input; }
    public void setInput(String input) { this.input = input; }
    public String getModel() { return model; }
    public void setModel(String model) { this.model = model; }
}
