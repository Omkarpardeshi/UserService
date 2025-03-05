package com.scaler.UserService.Controller;

import com.scaler.UserService.DTO.*;
import com.scaler.UserService.Models.Token;
import com.scaler.UserService.Models.User;
import com.scaler.UserService.Services.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/Users")
public class userController {

    private UserService userService;
    public userController(UserService userService){
        this.userService=userService;
    }

    @PostMapping("/login")
    public LoginResponceDTO login(@RequestBody LoginRequestDTO loginRequestDTO){
        Token token=userService.login(loginRequestDTO.getEmail(), loginRequestDTO.getPassword());
        LoginResponceDTO loginResponceDTO=new LoginResponceDTO();
        loginResponceDTO.setTokenValue(token.getValue());
        return loginResponceDTO;
    }
    @PostMapping("/signup")
    public UserDTO signup(@RequestBody SignupRequestDTO signupRequestDTO){
        User user=userService.signup(signupRequestDTO.getName(),signupRequestDTO.getEmail(), signupRequestDTO.getPassword());
        return UserDTO.from(user);
    }
    @GetMapping("/logout")
    public void logout(@RequestBody LogoutRequestDTO logoutRequestDTO){
        userService.logout(logoutRequestDTO.getTokenvalue());
//        return responce;

    }
    @GetMapping("/validate/{Token}")
    public ResponseEntity<UserDTO> validateToken(@PathVariable ("Token") String Token){
        User user=userService.Validate(Token);
        ResponseEntity<UserDTO> userDTOResponseEntity=null;
        if(user==null){
            userDTOResponseEntity=new ResponseEntity<>(
                    null,
                    HttpStatus.UNAUTHORIZED
            );
        }else{
            userDTOResponseEntity=new ResponseEntity<>(
                    UserDTO.from(user),
                    HttpStatus.OK
            );
        }

        return userDTOResponseEntity;
    }

    @GetMapping("/sample")
    public void sampleAPI(){
        System.out.println("Got a sample APT Request");
    }
}
