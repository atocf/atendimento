package br.com.atendimento.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import br.com.atendimento.dto.submotivo.SubMotivoDtoPost;
import br.com.atendimento.dto.submotivo.SubMotivoDtoPut;
import br.com.atendimento.entity.Analista;
import br.com.atendimento.entity.SubMotivo;
import br.com.atendimento.exception.RestExceptionCustom;
import br.com.atendimento.exception.error.ErrorObject;
import br.com.atendimento.services.AnalistaService;
import br.com.atendimento.services.SubMotivoService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping("/submotivo")
@Api(tags = "Sub Motivo", description = "Endpoint´s incluir, atualizar, deletar ou buscar um SubMotivo.")
public class SubMotivoController {
	
	@Autowired
	private AnalistaService analistaService;
	
	@Autowired
	private SubMotivoService service;

	@GetMapping
	@Cacheable(value = "listSubMotivo")
	@ApiOperation("Retorna a lista de SubMotivos.")
	public Page<SubMotivo> findAll(@RequestParam(value = "page", required = false, defaultValue = "0") int page,
			@RequestParam(value = "size", required = false, defaultValue = "50") int size,
			@RequestParam(value = "status", required = true, defaultValue = "Ativo") String status) {
		return service.findAll(page, size, status);
	}

	@GetMapping(path = { "/{id}" })
	@ApiOperation("Retorna o submotivo pelo {id}.")
	public ResponseEntity<?> findById(@PathVariable Long id) {
		return service.findById(id).map(record -> ResponseEntity.ok().body(record))
				.orElse(ResponseEntity.notFound().build());
	}
	
	@PostMapping
	@CacheEvict(value = "listSubMotivo", allEntries = true)
	@ApiOperation("Cria um novo submotivo.")
	public ResponseEntity<?> create(@RequestBody @Valid SubMotivoDtoPost submotivo) {
		Optional<Analista> analista = analistaService.findById(submotivo.getAnalista_id());
		
		if (analista.isPresent()) {
			return new ResponseEntity<SubMotivo>(service.save(submotivo.converter(analista.get())), HttpStatus.CREATED);
		}
		
		List<ErrorObject> errors = new ArrayList<ErrorObject>();
		errors = RestExceptionCustom.setListError(errors, analistaService.returnError(submotivo.getAnalista_id()));
		return ResponseEntity.badRequest()
				.body(RestExceptionCustom.getErrorResponse("SubMotivoDtoPost", HttpStatus.BAD_REQUEST, errors));
	}

	@PutMapping(value = "/{id}")
	@CacheEvict(value = "listSubMotivo", allEntries = true)
	@ApiOperation("Atualiza um submotivo pelo {id}.")
	public ResponseEntity<?> update(@PathVariable("id") Long id, @RequestBody @Valid SubMotivoDtoPut submotivo) {
		
		Optional<Analista> analista = analistaService.findById(submotivo.getAnalista_id());
		
		List<ErrorObject> errors = new ArrayList<ErrorObject>();		
		if (!analista.isPresent()) {
			errors = RestExceptionCustom.setListError(errors, analistaService.returnError(submotivo.getAnalista_id()));
			return ResponseEntity.badRequest()
					.body(RestExceptionCustom.getErrorResponse("SubMotivoDtoPost", HttpStatus.BAD_REQUEST, errors));	
		}		
		
		return service.findById(id).map(record -> {
			record.setAnalista(analista.get());
			record.setProduto(submotivo.getProduto());
			record.setTipopublico(submotivo.getTipopublico());
			record.setVariedadeproduto(submotivo.getVariedadeproduto());
			record.setMotivo(submotivo.getMotivo());
			record.setEquipe(submotivo.getEquipe());
			record.setStatus(submotivo.getStatus());
			record.setSincronismo(submotivo.getSincronismo());
			record.setCdtp004(submotivo.getCdtp004());
			record.setDgp001(submotivo.getDgp001());
			record.setDgp041(submotivo.getDgp041());
			record.setImp001(submotivo.getImp001());
			record.setImp003(submotivo.getImp003());
			SubMotivo updated = service.save(record);
			return ResponseEntity.ok().body(updated);
		}).orElse(ResponseEntity.notFound().build());
	}

	@DeleteMapping(path = { "/{id}" })
	@CacheEvict(value = "listSubMotivo", allEntries = true)
	@ApiOperation("Deleta o submotivo pelo {id}.")
	public ResponseEntity<?> delete(@PathVariable Long id) {
		return service.findById(id).map(record -> {
			service.deleteById(id);
			return ResponseEntity.accepted().build();
		}).orElse(ResponseEntity.notFound().build());
	}
}
