package com.scaler.UserService.DTO;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LogoutRequestDTO {
    private String Tokenvalue;

    public String getTokenvalue() {
        return Tokenvalue;
    }

    public void setTokenvalue(String tokenvalue) {
        Tokenvalue = tokenvalue;
    }
}
