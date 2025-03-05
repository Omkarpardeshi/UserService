package com.scaler.UserService.DTO;

import com.scaler.UserService.Models.Token;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LoginResponceDTO {
    private String tokenValue;

    public String getTokenValue() {
        return tokenValue;
    }

    public void setTokenValue(String tokenValue) {
        this.tokenValue = tokenValue;
    }
}
