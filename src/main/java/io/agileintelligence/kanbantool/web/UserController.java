package io.agileintelligence.kanbantool.web;


import io.agileintelligence.kanbantool.domain.User;
import io.agileintelligence.kanbantool.payload.JWTLoginSuccessResponse;
import io.agileintelligence.kanbantool.payload.LoginRequest;
import io.agileintelligence.kanbantool.security.JwtTokenProvider;
import io.agileintelligence.kanbantool.service.MapValidationErrorsService;
import io.agileintelligence.kanbantool.service.UserService;
import io.agileintelligence.kanbantool.validator.UserValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

import static io.agileintelligence.kanbantool.security.SecurityConstants.TOKEN_PREFIX;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private MapValidationErrorsService mapValidationErrorsService;

    @Autowired
    private UserValidator userValidator;

    @Autowired
    private JwtTokenProvider tokenProvider;

    @Autowired
    private AuthenticationManager authenticationManager;


    @PostMapping("/login")
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest, BindingResult result){
        ResponseEntity<?> errorMap = mapValidationErrorsService.MapValidationErrorsService(result);
        if(errorMap != null) return errorMap;

        Authentication authentication = authenticationManager.authenticate(

                new UsernamePasswordAuthenticationToken(
                        loginRequest.getUsername(),
                        loginRequest.getPassword()
                )
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);

        String jwt = TOKEN_PREFIX + tokenProvider.generateToken(authentication);

        return  ResponseEntity.ok(new JWTLoginSuccessResponse(true,jwt));


    }


    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@Valid @RequestBody User user, BindingResult result){
        userValidator.validate(user,result);
        ResponseEntity<?> errorMap = mapValidationErrorsService.MapValidationErrorsService(result);
        if(errorMap != null) return errorMap;

        User newUser = userService.saveOrUpdateUser(user);

        return new ResponseEntity<User>(newUser, HttpStatus.CREATED);

    }

}
