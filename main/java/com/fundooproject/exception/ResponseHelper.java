package com.fundooproject.exception;

import org.springframework.stereotype.Component;


import com.fundooproject.configuration.Response;
@Component
public class ResponseHelper {
	public static Response statusResponse(int status, String message, Object result) {
		Response statusResponse = new Response();
		statusResponse.setMessage(message);
		statusResponse.setStatus(status);
		statusResponse.setResult(result);
		return statusResponse;
	}

}
