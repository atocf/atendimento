package br.com.atendimento.controller;

import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.atendimento.client.Cdtp004Client;
import br.com.atendimento.client.Imp001Client;
import br.com.atendimento.services.Dgp001Service;
import br.com.atendimento.services.Dgp041Service;
import br.com.atendimento.services.Imp003Service;
import br.com.atendimento.wsdl.cdtp004.ConsultarDadosCartao;
import br.com.atendimento.wsdl.cdtp004.ConsultarDadosCartaoResponse;
import br.com.atendimento.wsdl.imp001.ObterContasCorrentesPessoa;
import br.com.atendimento.wsdl.imp001.ObterContasCorrentesPessoaResponse;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping("/integracao")
@Api(tags = "Integracao", description = "Endpoint´s de teste de integração")
public class IntegracaoController {
	
	@Autowired
	private Imp003Service imp003Service;
	
	@GetMapping(path = { "/imp003/pin/{cpf}" })
	@ApiOperation("Consultar o status do Pin Eletrônico pelo CPF: {cpf}.")
	public ResponseEntity<String> consultaStatusPin(@PathVariable String cpf) {
		return ResponseEntity.ok(imp003Service.consultaStatusPin(cpf));
	}
	
	@Autowired
	private Imp001Client imp001Client;
	
	@PostMapping("/imp001/conta")
    public ObterContasCorrentesPessoaResponse item(@RequestBody ObterContasCorrentesPessoa itemRequest) throws KeyManagementException, NoSuchAlgorithmException{
        return imp001Client.obterContasCorrentesPessoa(itemRequest);
    }
	
	@Autowired
	private Dgp041Service dgp041Service;
	
	@GetMapping(path = { "/dgp041/scopo/{cpf}" })
	@ApiOperation("Consultar o scopo pelo CPF: {cpf}.")
	public ResponseEntity<String> consultaScopo(@PathVariable String cpf) {
		return ResponseEntity.ok(dgp041Service.consultaScopo(cpf));
	}
	
	@Autowired
	private Dgp001Service dgp001Service;
	
	@GetMapping(path = { "/dgp001/cliente/{cpf}" })
	@ApiOperation("Consultar os dados cadastrais pelo CPF: {cpf}.")
	public ResponseEntity<?> consultaCliente(@PathVariable String cpf) {
		return ResponseEntity.ok(dgp001Service.consultaCliente(cpf));
	}
	
	@Autowired
	private Cdtp004Client cdtp004Client;
	
	@PostMapping("/cdtp004/cartao")
    public ConsultarDadosCartaoResponse item(@RequestBody ConsultarDadosCartao itemRequest) throws KeyManagementException, NoSuchAlgorithmException{
        return cdtp004Client.consultarDadosCartao(itemRequest);
    }
}
