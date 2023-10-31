package br.com.atendimento.gerador;

import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.atendimento.dto.ResponseMessage;
import br.com.atendimento.dto.importar.ResponseImportDto;
import br.com.atendimento.services.ExportarService;
import br.com.atendimento.services.ImportarService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping("/gerador")
@Api(tags = "Gerador", description = "Endpoint´s  para importação e exportação das planilhas de planilhas.")
public class GeradorController {

	@Autowired
	private ImportarService importarService;
	
	@Autowired
	private ExportarService exportarService;

	@PostMapping("/planilha/import")
	public  ResponseEntity<?> importPlanilhas(String sheet)  throws IOException, ParseException {
		try {
			return new ResponseEntity<ResponseImportDto>(importarService.importPlanilhas(sheet), HttpStatus.CREATED);
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseMessage(e.getMessage()));
		}		
	}
	
	@PostMapping("/planilha/export")
	public  ResponseEntity<?> exportPlanilhas(String dt_de, String dt_para)  throws IOException, ParseException {
		try {
			return new ResponseEntity<ResponseImportDto>(exportarService.exportPlanilhas(dt_de, dt_para), HttpStatus.CREATED);
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseMessage(e.getMessage()));
		}		
	}
	
	@GetMapping(path = { "/sincronismo" })
	@ApiOperation("Gerar planilha para enviar para sincronismo.")
	public void exportarSincronismo(HttpServletResponse response) throws IOException, ParseException {
		response.setContentType("application/octet-stream");
		DateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss");
		String currentDateTime = dateFormatter.format(new Date());

		String headerKey = "Content-Disposition";
		String headerValue = "attachment; filename=sincronizar_" + currentDateTime + ".xlsx";
		response.setHeader(headerKey, headerValue);

		exportarService.exportarSincronismo(response);
	}
}
