package br.com.atendimento.controller;

import java.io.IOException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.text.ParseException;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import br.com.atendimento.dto.analisar.ResponseAnalisarDto;
import br.com.atendimento.dto.analisar.ResponseAnalisarMassaDto;
import br.com.atendimento.dto.importar.CpfCnpjDto;
import br.com.atendimento.dto.importar.ResponseImportDto;
import br.com.atendimento.services.AnalisarService;
import br.com.atendimento.util.CsvUtils;
import io.swagger.annotations.Api;

@RestController
@RequestMapping("/analisar")
@Api(tags = "Analisar", description = "Endpoint´s  para analistar os chamados.")
public class AnalisarController {

	@Autowired
	private AnalisarService service;

	@PostMapping(value = "/ajustar/status")
	public ResponseEntity<?> ajustarStatus() throws IOException, ParseException {
		return new ResponseEntity<ResponseAnalisarDto>(service.ajustarStatus(), HttpStatus.CREATED);
	}

	@PostMapping()
	public ResponseEntity<?> analisar() throws IOException, ParseException {
		return new ResponseEntity<ResponseAnalisarDto>(service.analisar(), HttpStatus.CREATED);
	}

	@PostMapping(value = "/massa")
	public ResponseEntity<?> massa(@RequestBody @Valid ResponseAnalisarMassaDto massa)
			throws IOException, ParseException {
		return new ResponseEntity<ResponseAnalisarDto>(service.massa(massa), HttpStatus.CREATED);
	}

	@PostMapping(value = "/sincronizar")
	public ResponseEntity<?> sincronizar() throws IOException, ParseException {
		return new ResponseEntity<ResponseAnalisarDto>(service.sincronizar(), HttpStatus.CREATED);
	}
	
	@PostMapping("/redis")
	public ResponseEntity<?> redis(@RequestParam("file") MultipartFile fileCsv)
			throws IOException, ParseException, KeyManagementException, NoSuchAlgorithmException {
		return new ResponseEntity<ResponseImportDto>(
				service.redis(CsvUtils.read(CpfCnpjDto.class, fileCsv.getInputStream())),
				HttpStatus.CREATED);
	}
}
