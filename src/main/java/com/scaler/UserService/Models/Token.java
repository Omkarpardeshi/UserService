package com.scaler.UserService.Models;

import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@Entity
public class Token extends Base{
    private String value;
    @ManyToOne
    private User user;
    private Date expiryat;

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Date getExpiryat() {
        return expiryat;
    }

    public void setExpiryat(Date expiryat) {
        this.expiryat = expiryat;
    }
}
