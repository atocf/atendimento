package br.com.atendimento.controller;

import java.io.IOException;
import java.text.ParseException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.atendimento.dto.analisar.ResponseAnalisarDto;
import br.com.atendimento.services.AnalisarService;
import io.swagger.annotations.Api;

@RestController
@RequestMapping("/analisar")
@Api(tags = "Analisar", description = "Endpoint´s  para analistar os chamados.")
public class AnalisarController {

	@Autowired
	private AnalisarService service;

	@PostMapping()
	public ResponseEntity<?> analisar() throws IOException, ParseException {
		return new ResponseEntity<ResponseAnalisarDto>(service.analisar(), HttpStatus.CREATED);
	}
}
