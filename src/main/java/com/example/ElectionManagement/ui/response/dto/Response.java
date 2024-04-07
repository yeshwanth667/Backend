package com.example.ElectionManagement.ui.response.dto;

public class Response {

	private String message;

	private Object data;

	private int statusCode;

	private String statusMessage;
	
	
	// GETTERS AND SETTERS

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public Object getData() {
		return data;
	}

	public void setData(Object data) {
		this.data = data;
	}

	public int getStatusCode() {
		return statusCode;
	}

	public void setStatusCode(int statusCode) {
		this.statusCode = statusCode;
	}

	public String getStatusMessage() {
		return statusMessage;
	}

	public void setStatusMessage(String statusMessage) {
		this.statusMessage = statusMessage;
	}

	// DEFAULT CONSTRUCTOR
	
	public Response() {
		super();
		// TODO Auto-generated constructor stub
	}

	
	// CONSTRUCTOR WITH DIFFERENT ARGUMENTS

	public Response(String message, Object data, int statusCode, String statusMessage) {
		super();
		this.message = message;
		this.data = data;
		this.statusCode = statusCode;
		this.statusMessage = statusMessage;
	}
}
