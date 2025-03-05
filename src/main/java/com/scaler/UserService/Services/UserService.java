package com.scaler.UserService.Services;

import com.scaler.UserService.Models.Token;
import com.scaler.UserService.Models.User;

public interface UserService {

    Token login(String email,String password);
    User signup(String name,String email,String password);
    void logout(String TokenValue);
    User Validate(String Tokenvalue);
}
