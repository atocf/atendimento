package br.com.atendimento.exception.error;

import java.util.Calendar;
import java.util.List;

import org.springframework.http.HttpStatus;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class ErrorResponse {
	
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy hh:mm:ss")
	private Calendar timestamp = Calendar.getInstance();
	private String message;
	private HttpStatus status;
	private String objectName;
	private List<ErrorObject> errors;
	
	public ErrorResponse(String message, HttpStatus status, String objectName, List<ErrorObject> errors) {
		super();
		this.message = message;
		this.status = status;
		this.objectName = objectName;
		this.errors = errors;
	}
}


