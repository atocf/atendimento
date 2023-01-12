package br.com.atendimento.controller;

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

import br.com.atendimento.dto.status.StatusDtoPost;
import br.com.atendimento.dto.status.StatusDtoPut;
import br.com.atendimento.entity.Status;
import br.com.atendimento.services.StatusService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping("/status")
@Api(tags = "Status", description = "Endpoint´s incluir, atualizar, deletar ou buscar um Status.")
public class StatusController {
	
	@Autowired
	private StatusService service;

	@GetMapping
	@Cacheable(value = "listStatus")
	@ApiOperation("Retorna a lista de Status.")
	public Page<Status> findAll(@RequestParam(value = "page", required = false, defaultValue = "0") int page,
			@RequestParam(value = "size", required = false, defaultValue = "50") int size,
			@RequestParam(value = "ativo", required = true, defaultValue = "true") Boolean ativo) {
		return service.findAll(page, size, ativo);
	}

	@GetMapping(path = { "/{id}" })
	@ApiOperation("Retorna o status pelo {id}.")
	public ResponseEntity<?> findById(@PathVariable Long id) {
		return service.findById(id).map(record -> ResponseEntity.ok().body(record))
				.orElse(ResponseEntity.notFound().build());
	}
	
	@PostMapping
	@CacheEvict(value = "listStatus", allEntries = true)
	@ApiOperation("Cria um novo status.")
	public ResponseEntity<?> create(@RequestBody @Valid StatusDtoPost status) {
		return new ResponseEntity<Status>(service.save(status.converter()), HttpStatus.CREATED);
	}

	@PutMapping(value = "/{id}")
	@CacheEvict(value = "listStatus", allEntries = true)
	@ApiOperation("Atualiza um status pelo {id}.")
	public ResponseEntity<?> update(@PathVariable("id") Long id, @RequestBody @Valid StatusDtoPut status) {
		return service.findById(id).map(record -> {
			record.setAtivo(status.getAtivo());
			Status updated = service.save(record);
			return ResponseEntity.ok().body(updated);
		}).orElse(ResponseEntity.notFound().build());
	}

	@DeleteMapping(path = { "/{id}" })
	@CacheEvict(value = "listStatus", allEntries = true)
	@ApiOperation("Deleta o status pelo {id}.")
	public ResponseEntity<?> delete(@PathVariable Long id) {
		return service.findById(id).map(record -> {
			service.deleteById(id);
			return ResponseEntity.accepted().build();
		}).orElse(ResponseEntity.notFound().build());
	}
}
