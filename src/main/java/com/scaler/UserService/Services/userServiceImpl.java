package com.scaler.UserService.Services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.scaler.UserService.DTO.SendEmailDTO;
import com.scaler.UserService.Models.Token;
import com.scaler.UserService.Models.User;
import com.scaler.UserService.Repository.TokenRepository;
import com.scaler.UserService.Repository.UserRepository;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.Optional;

@Service
public class userServiceImpl implements UserService{

    private BCryptPasswordEncoder bCryptPasswordEncoder;
    private UserRepository userRepository;
    private TokenRepository tokenRepository;
    private KafkaTemplate<String, String> kafkaTemplate;
    private ObjectMapper objectMapper;


    public userServiceImpl(BCryptPasswordEncoder bCryptPasswordEncoder,UserRepository userRepository,TokenRepository tokenRepository,KafkaTemplate<String, String> kafkaTemplate,ObjectMapper objectMapper){
        this.bCryptPasswordEncoder=bCryptPasswordEncoder;
        this.userRepository=userRepository;
        this.tokenRepository=tokenRepository;
        this.kafkaTemplate=kafkaTemplate;
        this.objectMapper=objectMapper;
    }
    @Override
    public Token login(String email, String password) {
        Optional<User> optionalUser=userRepository.findByEmail(email);
        if(optionalUser.isEmpty()){
            return null;
        }
        User user= optionalUser.get();
        if(!bCryptPasswordEncoder.matches(password, user.getPassword())){
            return null;
        };

        Token token=new Token();
        token.setUser(optionalUser.get());
        token.setValue(RandomStringUtils.randomAlphanumeric(128));

        LocalDate localDate=LocalDate.now().plusDays(30);
        Date expriredate= Date.from(localDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
        token.setExpiryat(expriredate);


        return tokenRepository.save(token);
    }

    @Override
    public User signup(String name, String email, String password) {
        User user=new User();
        user.setName(name);
        user.setEmail(email);
        user.setPassword(bCryptPasswordEncoder.encode(password));
        SendEmailDTO sendEmailDTO=new SendEmailDTO();
        sendEmailDTO.setBody("Hello welcome to my life");
        sendEmailDTO.setSubject("Reg : Welcome to my life");
        sendEmailDTO.setEmail(email);
        try {
            kafkaTemplate.send(
                    "sendEmail",
                    objectMapper.writeValueAsString(sendEmailDTO)
            );
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

        return userRepository.save(user);
    }

    @Override
    public void logout(String TokenValue) {
//        Optional<Token> optionalToken=tokenRepository.findByValueAndDeletedAndExpiryatGreaterThan(Tokenvalue,false,new Date());
        Optional<Token> optionalToken=tokenRepository.findByValueAndDeletedAndExpiryatGreaterThan(TokenValue,false,new Date());
        if(optionalToken.isEmpty()){
            throw  new UsernameNotFoundException("User Not found");
        }
        else{
            Token token= optionalToken.get();
            token.setDeleted(true);
            tokenRepository.save(token);
//            return "user loged out";
        }

//        return null;
    }

    @Override
    public User Validate(String Tokenvalue) {
        Optional<Token> optionalToken=tokenRepository.findByValueAndDeletedAndExpiryatGreaterThan(Tokenvalue,false,new Date());
        if(optionalToken.isEmpty()){
            return null;
        }



        return optionalToken.get().getUser();
    }
}
