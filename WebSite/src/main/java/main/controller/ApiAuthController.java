package main.controller;

import com.fasterxml.jackson.annotation.JsonProperty;
import main.api.response.CaptchaResponse;
import main.api.response.CheckResponse;
import main.api.response.RegisterResponse;
import main.service.CaptchaService;
import main.service.RegisterService;
import main.service.dto.LoginForm;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class ApiAuthController {

    private final CheckResponse checkResponse;
    private final CaptchaService captchaService;
    private final RegisterService registerService;

    public ApiAuthController(CheckResponse checkResponse, CaptchaService captchaService, RegisterService registerService) {
        this.checkResponse = checkResponse;
        this.captchaService = captchaService;
        this.registerService = registerService;
    }

    @GetMapping("/check")
    private CheckResponse check (){
        return new CheckResponse();
    }

    @GetMapping("/captcha")
    private CaptchaResponse captcha () {
        return captchaService.getResponse();
    }

    @PostMapping(value = "/register")
    private RegisterResponse register (@RequestBody LoginForm form){
        return registerService.getResponse(form.getEmail(), form.getPassword(), form.getName(), form.getCaptcha(), form.getCaptchaSecret());
    }

}
