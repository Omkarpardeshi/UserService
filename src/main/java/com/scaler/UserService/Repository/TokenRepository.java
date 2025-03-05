package com.scaler.UserService.Repository;

import com.scaler.UserService.Models.Token;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.Optional;

@Repository
public interface TokenRepository extends JpaRepository<Token,Long> {
    Token save(Token token);


    Optional<Token> findByValueAndDeletedAndExpiryatGreaterThan(String value, boolean deleted, Date date );
}
