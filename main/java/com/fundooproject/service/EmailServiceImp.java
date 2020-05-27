package com.fundooproject.service;

import com.fundooproject.dto.LoginDto;
import com.fundooproject.dto.RabbitMqDto;
import com.fundooproject.exception.UserException;
import com.fundooproject.model.User;
import com.fundooproject.repository.IUserRepository;
import com.fundooproject.utility.JwtTokenUtil;
import com.fundooproject.utility.RabbitMqImp;
import java.time.LocalDateTime;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;

@Service
public class EmailServiceImp implements IEmailService {

    @Autowired
    private IUserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Autowired
    private RabbitMqDto rabbitMqDto;

    @Autowired
    private RabbitMqImp rabbitMq;

    @Override
    public Long validateEmail(String email) {
        return userRepository.findByEmail(email)
                .map(user -> {
                    String token = jwtTokenUtil.createToken(user.getUser_id());
                    String body =   token ;
                    System.out.println("token = " +token);
                    rabbitMqDto.setTo(email);
                    rabbitMqDto.setFrom("nandiniswaraj95@gmail.com");
                    rabbitMqDto.setSubject("User Verification");
                    rabbitMqDto.setBody(body);
                    rabbitMq.sendMessageToQueue(rabbitMqDto);
                    rabbitMq.send(rabbitMqDto);
                    return user.getUser_id();
                })
                .orElseThrow(() -> new UserException(UserException.exceptionType.INVALID_EMAIL_ID));
    }
    
      
    @Override
    public User setPassword(LoginDto loginDto, String token) {
        long id = jwtTokenUtil.decodeToken(token);
        LocalDateTime localDateTime = LocalDateTime.now();
        return userRepository.findById(id)
                .map(user -> {
                    String password = passwordEncoder.encode(loginDto.password);
                    user.setPassword(password);
                    user.setUserModifyDate(localDateTime);

                    return user;
                })
                .map(userRepository::save).get();
    }

    
    @Override
    public void verification(String token) {
    	long id = jwtTokenUtil.decodeToken(token);
    		  userRepository.findById(id)
                .map(user -> {
                	if(user.isVerify()) 
               throw new UserException(UserException.exceptionType.EMAIL_ALREADY_VERIFIED);
                		user.setVerify(true);
                 return user;
                }
                )
                .map(userRepository::save).get();
                }
    	
}