package com.fundooproject.model;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fundooproject.dto.UserDto;



@Getter
@Setter
@Entity
@Table(name = "UserData")
public class User implements Serializable {
	
	private static final long serialVersionUID = 1L;
    
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long user_id;
	private String firstName;
	private String lastName;
	private String password;
	@Column(unique = true,nullable = false )
	private String email;
	private LocalDateTime userRegistrationDate;
	private LocalDateTime userModifyDate;
	private boolean isVerify=false;


	public User(UserDto userDto) {
		this.firstName=userDto.firstName;
		this.lastName=userDto.lastName;
		this.password=userDto.password;
		this.email=userDto.email;
	    
	}
	public User() {
		
	}


}