package com.fundooproject.dto;

import lombok.Getter;
import lombok.Setter;

import org.springframework.stereotype.Component;

import java.io.Serializable;

@Component
@Getter
@Setter
public class RabbitMqDto implements Serializable  {

	private static final long serialVersionUID = 1L;

    private String to;
    private String from;
    private String subject;
    private String body;
    
	
}