package main.controller;

import main.api.response.CaptchaResponse;
import main.api.response.LoginResponse;
import main.api.response.RegisterResponse;
import main.model.User;
import main.model.repositories.UserRepository;
import main.service.CaptchaService;
import main.service.LoginService;
import main.service.RegisterService;
import main.api.request.LoginForm;
import main.api.request.LoginRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.security.Principal;

@RestController
@RequestMapping("/api/auth")
public class ApiAuthController {

    private final CaptchaService captchaService;
    private final RegisterService registerService;
    private final AuthenticationManager authenticationManager;
    private final LoginService loginService;
    private final UserRepository userRepository;

    @Autowired
    public ApiAuthController(CaptchaService captchaService, RegisterService registerService, AuthenticationManager authenticationManager, LoginResponse loginResponse, LoginService loginService, UserRepository userRepository) {
        this.captchaService = captchaService;
        this.registerService = registerService;
        this.authenticationManager = authenticationManager;
        this.loginService = loginService;
        this.userRepository = userRepository;
    }

    @GetMapping("/check")
    private LoginResponse check (@AuthenticationPrincipal Principal principal) {
        if (principal != null) {
            User user = userRepository.findByEmail(principal.getName());
            return loginService.getLoginResponse(user);
        }
         return new LoginResponse();
    }

    @GetMapping("/captcha")
    private CaptchaResponse captcha () {
        return captchaService.getResponse();
    }

    @PostMapping(value = "/register")
    private RegisterResponse register (@RequestBody LoginForm form){
        return registerService.getResponse(form.getEmail(), form.getPassword(), form.getName(), form.getCaptcha(), form.getCaptchaSecret());
    }

    @PostMapping("/login")
    public LoginResponse login (@RequestBody LoginRequest loginRequest){
        User user = userRepository.findByEmail(loginRequest.getEmail());
        if (user != null) {
            Authentication auth = authenticationManager.authenticate(
                            new UsernamePasswordAuthenticationToken(loginRequest.getEmail(),
                                    loginRequest.getPassword()));
            SecurityContextHolder.getContext().setAuthentication(auth);
        }
        return loginService.getLoginResponse(user);
    }

    @GetMapping("/logout")
    private LoginResponse logout(HttpServletRequest request, HttpServletResponse response){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        new SecurityContextLogoutHandler().logout(request, response, auth);
        LoginResponse loginResponse = new LoginResponse();
        loginResponse.setResult(true);
        return loginResponse;
    }

}
