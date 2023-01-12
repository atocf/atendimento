package br.com.atendimento.controller;

import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.atendimento.services.ExportarService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping("/exportar")
@Api(tags = "Exportação", description = "Endpoint´s  para exportação de planilhas.")
public class ExportarController {

	@Autowired
	private ExportarService service;

	@GetMapping(path = { "/sincronismo" })
	@ApiOperation("Gerar planilha para enviar para sincronismo.")
	public void exportarSincronismo(HttpServletResponse response) throws IOException, ParseException {
		response.setContentType("application/octet-stream");
		DateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss");
		String currentDateTime = dateFormatter.format(new Date());

		String headerKey = "Content-Disposition";
		String headerValue = "attachment; filename=sincronizar_" + currentDateTime + ".xlsx";
		response.setHeader(headerKey, headerValue);
		
		service.exportarSincronismo(response);
	}
	
	@GetMapping(path = { "/atendimento/pf" })
	@ApiOperation("Gerar planilha para atendimento PF.")
	public void exportarAtendimentoPf(HttpServletResponse response) throws IOException, ParseException {
		response.setContentType("application/octet-stream");
		DateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss");
		String currentDateTime = dateFormatter.format(new Date());

		String headerKey = "Content-Disposition";
		String headerValue = "attachment; filename=atendimento_pf_" + currentDateTime + ".xlsx";
		response.setHeader(headerKey, headerValue);
		
		service.exportarAtendimentoPf(response);
	}
	
	@GetMapping(path = { "/atendimento/pj" })
	@ApiOperation("Gerar planilha para atendimento PJ.")
	public void exportarAtendimentoPj(HttpServletResponse response) throws IOException, ParseException {
		response.setContentType("application/octet-stream");
		DateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss");
		String currentDateTime = dateFormatter.format(new Date());

		String headerKey = "Content-Disposition";
		String headerValue = "attachment; filename=atendimento_pj_" + currentDateTime + ".xlsx";
		response.setHeader(headerKey, headerValue);
		
		service.exportarAtendimentoPj(response);
	}
}
