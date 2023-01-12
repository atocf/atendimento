package br.com.atendimento.exception;

import java.util.ArrayList;
import java.util.List;

import org.springframework.http.HttpStatus;

import br.com.atendimento.exception.error.ErrorObject;
import br.com.atendimento.exception.error.ErrorResponse;

public class RestExceptionCustom {
	
	public static List<ErrorObject> setListError(List<ErrorObject> list, ErrorObject error) {
		list.add(error);		
		return list;
	}
	
	public static ErrorResponse getErrorResponse(String objeto_name, HttpStatus status, List<ErrorObject> errors) {
		return new ErrorResponse("Requisição possui campos inválidos!", status, objeto_name, errors);
	}
	
	public static ErrorResponse getErrorResponse(String objeto_name, HttpStatus status, ErrorObject error) {
		List<ErrorObject> errors = new ArrayList<ErrorObject>();
		errors.add(error);
		return new ErrorResponse("Requisição possui campos inválidos!", status, objeto_name, errors);
	}
	
}