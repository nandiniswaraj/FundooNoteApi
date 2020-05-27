package com.fundooproject.utility;

import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

import com.fundooproject.dto.RabbitMqDto;

@Component
public class RabbitMqImp implements IRabbitMq {

	@Autowired
	private AmqpTemplate rabbitTemplate;

	@Autowired
	private JavaMailSender javaMailSender;

   	@Override
	public void sendMessageToQueue(RabbitMqDto rabbitMqDto) {
		final String exchange = "QueueExchangeConn";
		final String routingKey = "RoutingKey";
		rabbitTemplate.convertAndSend(exchange, routingKey, rabbitMqDto);

	}

	@Override
	@RabbitListener(queues = "${spring.rabbitmq.template.default-receive-queue}")
	public void send(RabbitMqDto email) {
		SimpleMailMessage message = new SimpleMailMessage();
		message.setTo(email.getTo());
		message.setFrom(email.getFrom());
		message.setSubject(email.getSubject());
		message.setText(email.getBody());
		javaMailSender.send(message);
		System.out.println("Mail Sent Succesfully");
	}

	@Override
	@RabbitListener(queues = "${spring.rabbitmq.template.default-receive-queue}")
	public void receiveMessage(RabbitMqDto email) {
		send(email);
	}

}

