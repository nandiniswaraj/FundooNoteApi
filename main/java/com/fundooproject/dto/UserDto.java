package com.fundooproject.dto;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;
@Setter
@Getter
public class  UserDto {

		@NotEmpty(message = "Please Enter First Name")
		public String firstName;

		@NotEmpty(message = "Please Enter Last Name")
		public String lastName;

		@NotEmpty(message = "Please Enter Password")
		public String password;

		@Pattern(regexp = ("^[_A-Za-z0-9-]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$"))
		@Email(message = "Please Enter Valid Email")
		@NotEmpty(message = "Please Enter Email")
		public String email;

		

		

}
