package main.api.response;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.TreeMap;

public class PostMethodResponse {

    private boolean result;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private TreeMap <String, String> errors;

    public boolean isResult() {
        return result;
    }

    public void setResult(boolean result) {
        this.result = result;
    }

    public TreeMap<String, String> getErrors() {
        return errors;
    }

    public void setErrors(TreeMap<String, String> errors) {
        this.errors = errors;
    }
}
