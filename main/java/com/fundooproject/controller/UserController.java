package com.fundooproject.controller;

import com.fundooproject.configuration.ApplicationConfig;
import com.fundooproject.dto.LoginDto;
import com.fundooproject.dto.UserDto;
import com.fundooproject.dto.TokenMatch;
import com.fundooproject.model.User;
import com.fundooproject.responsedto.UserResponseDTO;
import com.fundooproject.service.IEmailService;
import com.fundooproject.service.IUserService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import javax.persistence.Cacheable;
import javax.validation.Valid;

@RequestMapping(value = "/userData")
@RestController
public class UserController {

	    @Autowired
	    private IEmailService service;

	    @Autowired
	    private IUserService userService;

	     
	    @PostMapping("/registration")
	    @ApiOperation("User Register API")
	    public ResponseEntity<UserResponseDTO> registration(@RequestBody @Valid UserDto userDto) {
	        User user = userService.registerUser(userDto);
            service.validateEmail(userDto.email);
	        return new ResponseEntity<>(new UserResponseDTO(user,
	                ApplicationConfig.getMessageAccessor().getMessage("101")), HttpStatus.CREATED);
	    }
	    
	    @PostMapping("/verifyEmail")
	    @ApiOperation("email verification API")
	    public ResponseEntity<UserResponseDTO> verifyEmail(@RequestHeader String token) {
	        service.verification(token);
	        return new ResponseEntity<>(new UserResponseDTO(token,
	                ApplicationConfig.getMessageAccessor().getMessage("107")), HttpStatus.OK);
	    }
	   
	    @PostMapping("/login")
	    @ApiOperation("User Login API")
	    public ResponseEntity<UserResponseDTO> login(@Valid @RequestBody LoginDto loginDto) {
	        long userId = userService.loginUser(loginDto);
	        return new ResponseEntity<>(new UserResponseDTO(userId,
	                ApplicationConfig.getMessageAccessor().getMessage("102")), HttpStatus.OK);
	    }

	    @PutMapping("/forgotPassword/{email}")
	    @ApiOperation("Forgot Password API")
	    public ResponseEntity<UserResponseDTO> forgot(@PathVariable String email) {
	        Long userId = service.validateEmail(email);
	        return new ResponseEntity<>(new UserResponseDTO(userId,
	                ApplicationConfig.getMessageAccessor().getMessage("103")), HttpStatus.ACCEPTED);
	    }

	    @PutMapping("/reset")
	    @ApiOperation("Reset Password API")
	    public ResponseEntity<UserResponseDTO> resetPassword(@RequestHeader String token,
	                                                         @Valid @RequestBody LoginDto loginDto) {
	        User user = service.setPassword(loginDto, token);
	        return new ResponseEntity<>(new UserResponseDTO(user,
	                ApplicationConfig.getMessageAccessor().getMessage("104")), HttpStatus.ACCEPTED);
	    }

	   
	  }