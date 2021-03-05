package main.api.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import main.service.dto.DTOUserLoginResponse;
import org.springframework.stereotype.Component;

@Component
public class LoginResponse {

    private boolean result;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private DTOUserLoginResponse user;

    public boolean isResult() {
        return result;
    }

    public void setResult(boolean result) {
        this.result = result;
    }

    public DTOUserLoginResponse getUser() {
        return user;
    }

    public void setUser(DTOUserLoginResponse user) {
        this.user = user;
    }
}
