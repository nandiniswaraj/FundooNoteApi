package com.fundooproject.service;

import com.fundooproject.dto.LoginDto;
import com.fundooproject.dto.RabbitMqDto;
import com.fundooproject.dto.UserDto;
import com.fundooproject.exception.UserException;
import com.fundooproject.model.Note;
import com.fundooproject.model.User;
import com.fundooproject.repository.IUserRepository;
import com.fundooproject.utility.JwtTokenUtil;
import com.fundooproject.utility.RabbitMqImp;
import com.fundooproject.utility.RedisTempl;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import java.util.UUID;


@Service
public class UserServiceImp implements IUserService {
	 
	private static final Object userDto = null;
	@Autowired
	private RabbitMqDto rabbitMqDto;
    @Autowired
	private RabbitMqImp rabbitMq;
	@Autowired
	private IEmailService service;
    @Autowired
    private IUserRepository userRepository;
    @Autowired
    private JwtTokenUtil jwtTokenUtil;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private ModelMapper mapper;
    @Autowired
    private RedisTempl<Object> redis;

    private String redisKey = "Key";
    @Override
    public User registerUser(UserDto userDto) {
        if (validateUser(userDto.email)) {
            throw new UserException(UserException.exceptionType.USER_ALREADY_EXIST);
        }
       // ModelMapper mapper = new ModelMapper();
		User user = mapper.map(userDto, User.class);
        LocalDateTime localDateTime = LocalDateTime.now();
        user.setUserRegistrationDate(localDateTime);
        user.setPassword(passwordEncoder.encode(userDto.password));
        return userRepository.save(user);
    }

    @Override
    public Long loginUser(LoginDto loginDto) {
        User user = userRepository.findByEmail(loginDto.email)
             .orElseThrow(() -> new UserException(UserException.exceptionType.INVALID_EMAIL_ID));
        if (!passwordEncoder.matches(loginDto.password, user.getPassword()))
            throw new UserException(UserException.exceptionType.INVALID_PASSWORD);
        
           String token = jwtTokenUtil.createToken(user.getUser_id());
           redis.putMap(redisKey, user.getEmail(), token);
           String body =   token ;
           rabbitMqDto.setTo(loginDto.email);
           rabbitMqDto.setFrom("nandiniswaraj95@gmail.com");
           rabbitMqDto.setSubject("Login Email Verification");
           rabbitMqDto.setBody(body);
           rabbitMq.sendMessageToQueue(rabbitMqDto);
           rabbitMq.send(rabbitMqDto);
           return user.getUser_id();
          }

    @Override
    public Boolean validateUser(String email) {
        Optional<User> byEmail = userRepository.findByEmail(email);
        if (byEmail.isPresent())
            return true;
        return false;
    }

}

