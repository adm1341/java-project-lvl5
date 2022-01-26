package hexlet.code.app.controller;

import hexlet.code.app.dto.LoginDto;
import hexlet.code.app.service.TokenAuthenticationService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static hexlet.code.app.controller.AuthController.LOGIN;


@AllArgsConstructor
@RestController
@RequestMapping("${base-url}" + LOGIN)
public class AuthController {

    public static final String LOGIN = "/login";

    private final TokenAuthenticationService authenticationService;

    @PostMapping
    public String login(@RequestBody final LoginDto loginDto) {
        return authenticationService.login(loginDto.getUsername(), loginDto.getPassword());
    }

}