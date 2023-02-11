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

import br.com.atendimento.dto.kibana.KibanaDtoPost;
import br.com.atendimento.dto.kibana.KibanaDtoPut;
import br.com.atendimento.entity.Kibana;
import br.com.atendimento.services.KibanaService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping("/kibana")
@Api(tags = "Kibana", description = "Endpoint´s incluir, atualizar, deletar ou buscar uma consulta.")
public class KibanaController {
	
	@Autowired
	private KibanaService service;

	@GetMapping
	@Cacheable(value = "listKibana")
	@ApiOperation("Retorna a lista de Mensagens.")
	public Page<Kibana> findAll(@RequestParam(value = "page", required = false, defaultValue = "0") int page,
			@RequestParam(value = "size", required = false, defaultValue = "50") int size,
			@RequestParam(value = "ativo", required = true, defaultValue = "true") Boolean ativo) {
		return service.findAll(page, size, ativo);
	}

	@GetMapping(path = { "/{id}" })
	@ApiOperation("Retorna a consulta pelo {id}.")
	public ResponseEntity<?> findById(@PathVariable Long id) {
		return service.findById(id).map(record -> ResponseEntity.ok().body(record))
				.orElse(ResponseEntity.notFound().build());
	}
	
	@PostMapping
	@CacheEvict(value = "listKibana", allEntries = true)
	@ApiOperation("Cria uma nova consulta.")
	public ResponseEntity<?> create(@RequestBody @Valid KibanaDtoPost status) {
		return new ResponseEntity<Kibana>(service.save(status.converter()), HttpStatus.CREATED);
	}

	@PutMapping(value = "/{id}")
	@CacheEvict(value = "listKibana", allEntries = true)
	@ApiOperation("Atualiza uma consulta pelo {id}.")
	public ResponseEntity<?> update(@PathVariable("id") Long id, @RequestBody @Valid KibanaDtoPut link) {
		return service.findById(id).map(record -> {
			record.setAtivo(link.getAtivo());
			record.setDescricao(link.getDescricao());
			record.setLink(link.getLink());
			Kibana updated = service.save(record);
			return ResponseEntity.ok().body(updated);
		}).orElse(ResponseEntity.notFound().build());
	}

	@DeleteMapping(path = { "/{id}" })
	@CacheEvict(value = "listKibana", allEntries = true)
	@ApiOperation("Deleta a consulta pelo {id}.")
	public ResponseEntity<?> delete(@PathVariable Long id) {
		return service.findById(id).map(record -> {
			service.deleteById(id);
			return ResponseEntity.accepted().build();
		}).orElse(ResponseEntity.notFound().build());
	}
}
