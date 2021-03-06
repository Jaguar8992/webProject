package main.controller;

import main.api.request.PasswordRequest;
import main.api.request.RestoreRequest;
import main.api.response.CaptchaResponse;
import main.api.response.LoginResponse;
import main.api.response.PostMethodResponse;
import main.api.response.ResultResponse;
import main.model.User;
import main.model.repositories.UserRepository;
import main.api.request.LoginForm;
import main.api.request.LoginRequest;
import main.service.auth.LoginService;
import main.service.auth.PasswordRestoreService;
import main.service.auth.RegisterService;
import main.service.auth.RestoreService;
import main.service.general.CaptchaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.web.bind.annotation.*;

import javax.mail.MessagingException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.net.UnknownHostException;
import java.security.Principal;

@RestController
@RequestMapping("/api/auth")
public class ApiAuthController {

    private final CaptchaService captchaService;
    private final RegisterService registerService;
    private final AuthenticationManager authenticationManager;
    private final LoginService loginService;
    private final UserRepository userRepository;
    private final RestoreService restoreService;
    private final PasswordRestoreService passwordRestoreService;

    @Autowired
    public ApiAuthController(CaptchaService captchaService, RegisterService registerService, AuthenticationManager authenticationManager, LoginResponse loginResponse, LoginService loginService, UserRepository userRepository, RestoreService restoreService, PasswordRestoreService passwordRestoreService) {
        this.captchaService = captchaService;
        this.registerService = registerService;
        this.authenticationManager = authenticationManager;
        this.loginService = loginService;
        this.userRepository = userRepository;
        this.restoreService = restoreService;
        this.passwordRestoreService = passwordRestoreService;
    }

    @GetMapping("/check")
    private ResponseEntity check (@AuthenticationPrincipal Principal principal) {
        if (principal != null) {
            User user = userRepository.findByEmail(principal.getName());
            return loginService.getLoginResponse(user);
        }
         return ResponseEntity.ok().body(new LoginResponse());
    }

    @GetMapping("/captcha")
    private CaptchaResponse captcha () {
        return captchaService.getResponse();
    }

    @PostMapping(value = "/register")
    private ResponseEntity register (@RequestBody LoginForm form){
        return registerService.getResponse(form.getEmail(), form.getPassword(), form.getName(), form.getCaptcha(), form.getCaptchaSecret());
    }

    @PostMapping("/login")
    public ResponseEntity login (@RequestBody LoginRequest loginRequest){
        User user = userRepository.findByEmail(loginRequest.getEmail());
        if (user == null){
            return ResponseEntity.ok().body(new ResultResponse());
        }
        else if (new BCryptPasswordEncoder(12).
                        matches(loginRequest.getPassword(), user.getPassword())) {
            Authentication auth = authenticationManager.authenticate(
                            new UsernamePasswordAuthenticationToken(loginRequest.getEmail(),
                                    loginRequest.getPassword()));
            SecurityContextHolder.getContext().setAuthentication(auth);
            return loginService.getLoginResponse(user);
        }
        else return ResponseEntity.ok().body(new ResultResponse());
    }

    @GetMapping("/logout")
    private LoginResponse logout(HttpServletRequest request, HttpServletResponse response){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        new SecurityContextLogoutHandler().logout(request, response, auth);
        LoginResponse loginResponse = new LoginResponse();
        loginResponse.setResult(true);
        return loginResponse;
    }

    @PostMapping("/restore")
    private ResultResponse restore (@RequestBody RestoreRequest request, HttpServletRequest servletRequest) throws MessagingException, UnknownHostException {

        String address = servletRequest.getHeader("Origin");
        return restoreService.getResponse(request.getEmail(), address);
    }

    @PostMapping("/password")
    private PostMethodResponse password (@RequestBody PasswordRequest passwordRequest){
        return passwordRestoreService.getResponse(passwordRequest.getCode(),
                passwordRequest.getPassword(), passwordRequest.getCaptcha(),
                passwordRequest.getCaptchaSecret());

    }

}
