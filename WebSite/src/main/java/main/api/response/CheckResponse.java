package main.api.response;

import org.springframework.stereotype.Component;

@Component
public class CheckResponse {

    private boolean result = false;

    public boolean isResult() {
        return result;
    }

    public void setResult(boolean result) {
        this.result = result;
    }
}
