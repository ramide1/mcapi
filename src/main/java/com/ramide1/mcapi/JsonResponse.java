package com.ramide1.mcapi;

public class JsonResponse {
    private String response;
    private boolean error;

    public JsonResponse(String response, boolean error) {
        this.response = response;
        this.error = error;
    }

    public String getResponse() {
        return response;
    }

    public boolean getError() {
        return error;
    }

    public void setResponse(String response) {
        this.response = response;
    }

    public void setError(boolean error) {
        this.error = error;
    }
}