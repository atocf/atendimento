package br.com.atendimento.controller;

import java.io.IOException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.text.ParseException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import br.com.atendimento.dto.ResponseMessage;
import br.com.atendimento.dto.importar.ImportarAberturaDto;
import br.com.atendimento.dto.importar.ImportarBacklogDto;
import br.com.atendimento.dto.importar.ResponseImportDto;
import br.com.atendimento.excel.PlanilhaExcelImport;
import br.com.atendimento.services.ImportarService;
import br.com.atendimento.util.CsvUtils;
import io.swagger.annotations.Api;

@RestController
@RequestMapping("/importar")
@Api(tags = "Importação", description = "Endpoint´s  para importação de planilhas.")
public class ImportarController {

	@Autowired
	private ImportarService service;

	@PostMapping("/integrall/abertura")
	public ResponseEntity<?> importarAbertura(@RequestParam("file") MultipartFile fileCsv)
			throws IOException, ParseException {
		return new ResponseEntity<ResponseImportDto>(
				service.importarAbertura(CsvUtils.read(ImportarAberturaDto.class, fileCsv.getInputStream())),
				HttpStatus.CREATED);
	}

	@PostMapping("/integrall/backlog")
	public ResponseEntity<?> importarBacklog(@RequestParam("file") MultipartFile fileCsv)
			throws IOException, ParseException, KeyManagementException, NoSuchAlgorithmException {
		return new ResponseEntity<ResponseImportDto>(
				service.importarBacklog(CsvUtils.read(ImportarBacklogDto.class, fileCsv.getInputStream())),
				HttpStatus.CREATED);
	}

	@PostMapping("/planilha/pf/{sheet}")
	public ResponseEntity<ResponseMessage> planilhaFilePf(@RequestParam("file") MultipartFile file, String sheet) {
		String message = "";

		if (PlanilhaExcelImport.hasExcelFormat(file)) {
			try {
				service.importarPlanilhaPf(file, sheet);

				message = "Uploaded the file successfully: " + file.getOriginalFilename();
				return ResponseEntity.status(HttpStatus.OK).body(new ResponseMessage(message));
			} catch (Exception e) {
				message = "Could not upload the file: " + file.getOriginalFilename() + "!";
				return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(new ResponseMessage(message));
			}
		}

		message = "Please upload an excel file!";
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseMessage(message));
	}
	
	@PostMapping("/planilha/pj/{sheet}")
	public ResponseEntity<ResponseMessage> planilhaFilePj(@RequestParam("file") MultipartFile file, String sheet) {
		String message = "";

		if (PlanilhaExcelImport.hasExcelFormat(file)) {
			try {
				service.importarPlanilhaPj(file, sheet);

				message = "Uploaded the file successfully: " + file.getOriginalFilename();
				return ResponseEntity.status(HttpStatus.OK).body(new ResponseMessage(message));
			} catch (Exception e) {
				message = "Could not upload the file: " + file.getOriginalFilename() + "!";
				return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(new ResponseMessage(message));
			}
		}

		message = "Please upload an excel file!";
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseMessage(message));
	}
}
