package com.fundooproject.service;

import com.fundooproject.dto.LoginDto;
import com.fundooproject.dto.UserDto;
import com.fundooproject.model.User;
import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface IUserService {

	User registerUser(UserDto userDto);
	Long loginUser(LoginDto loginDto);
	Boolean validateUser(String email);
	}
