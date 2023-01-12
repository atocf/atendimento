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

import br.com.atendimento.dto.analista.AnalistaDtoPost;
import br.com.atendimento.dto.analista.AnalistaDtoPut;
import br.com.atendimento.entity.Analista;
import br.com.atendimento.services.AnalistaService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping("/analista")
@Api(tags = "Analista", description = "Endpoint´s incluir, atualizar, deletar ou buscar uma/um Analista.")
public class AnalistaController {
	
	@Autowired
	private AnalistaService service;

	@GetMapping
	@Cacheable(value = "listAnalista")
	@ApiOperation("Retorna a lista de Analistas.")
	public Page<Analista> findAll(@RequestParam(value = "page", required = false, defaultValue = "0") int page,
			@RequestParam(value = "size", required = false, defaultValue = "50") int size,
			@RequestParam(value = "ativo", required = true, defaultValue = "true") Boolean ativo) {
		return service.findAll(page, size, ativo);
	}

	@GetMapping(path = { "/{id}" })
	@ApiOperation("Retorna a/o analista pelo {id}.")
	public ResponseEntity<?> findById(@PathVariable Long id) {
		return service.findById(id).map(record -> ResponseEntity.ok().body(record))
				.orElse(ResponseEntity.notFound().build());
	}
	
	@PostMapping
	@CacheEvict(value = "listAnalista", allEntries = true)
	@ApiOperation("Cria um/uma nova/novo analista.")
	public ResponseEntity<?> create(@RequestBody @Valid AnalistaDtoPost analista) {
		return new ResponseEntity<Analista>(service.save(analista.converter()), HttpStatus.CREATED);
	}

	@PutMapping(value = "/{id}")
	@CacheEvict(value = "listAnalista", allEntries = true)
	@ApiOperation("Atualiza uma/um analista pelo {id}.")
	public ResponseEntity<?> update(@PathVariable("id") Long id, @RequestBody @Valid AnalistaDtoPut analista) {
		return service.findById(id).map(record -> {
			record.setAtivo(analista.getAtivo());
			Analista updated = service.save(record);
			return ResponseEntity.ok().body(updated);
		}).orElse(ResponseEntity.notFound().build());
	}

	@DeleteMapping(path = { "/{id}" })
	@CacheEvict(value = "listAnalista", allEntries = true)
	@ApiOperation("Deleta a/o analista pelo {id}.")
	public ResponseEntity<?> delete(@PathVariable Long id) {
		return service.findById(id).map(record -> {
			service.deleteById(id);
			return ResponseEntity.accepted().build();
		}).orElse(ResponseEntity.notFound().build());
	}
}
