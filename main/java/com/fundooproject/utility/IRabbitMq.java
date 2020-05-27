package com.fundooproject.utility;

import com.fundooproject.dto.RabbitMqDto;

public interface IRabbitMq {

	void sendMessageToQueue(RabbitMqDto rabbitMqDto); 
	void receiveMessage(RabbitMqDto rabbitMqDto); 
	void send(RabbitMqDto rabbitMqDto);

}
