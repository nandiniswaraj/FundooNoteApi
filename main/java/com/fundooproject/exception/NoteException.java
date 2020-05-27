package com.fundooproject.exception;

import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus
public class NoteException extends RuntimeException {

	private static final long serialVersionUID = 1L;
	int code;
	String msg;

	public NoteException() {
	}

	public NoteException(int code, String msg) {
		super(msg);
		this.code = code;

	}

	public NoteException(String msg) {
		super(msg); 
	}

	@Override
	public String toString() {
		return "UserException [code=" + code + ", msg=" + msg + "]";
	}

}
