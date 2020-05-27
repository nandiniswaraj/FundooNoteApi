package com.fundooproject.service;

import com.fundooproject.dto.LoginDto;
import com.fundooproject.model.User;

public interface IEmailService {

	Long validateEmail(String email);
	User setPassword(LoginDto loginDto, String token);
	void verification(String token);
	

}
