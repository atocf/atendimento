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

import br.com.atendimento.dto.causaraiz.CausaRaizDtoPost;
import br.com.atendimento.dto.causaraiz.CausaRaizDtoPut;
import br.com.atendimento.entity.CausaRaiz;
import br.com.atendimento.services.CausaRaizService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping("/causaraiz")
@Api(tags = "Causa Raiz", description = "Endpoint´s incluir, atualizar, deletar ou buscar uma causa raiz.")
public class CausaRaizController {
	
	@Autowired
	private CausaRaizService service;

	@GetMapping
	@Cacheable(value = "listCausaRaiz")
	@ApiOperation("Retorna a lista de causa raizes.")
	public Page<CausaRaiz> findAll(@RequestParam(value = "page", required = false, defaultValue = "0") int page,
			@RequestParam(value = "size", required = false, defaultValue = "50") int size,
			@RequestParam(value = "ativo", required = true, defaultValue = "true") Boolean ativo) {
		return service.findAll(page, size, ativo);
	}

	@GetMapping(path = { "/{id}" })
	@ApiOperation("Retorna a causa raiz pelo {id}.")
	public ResponseEntity<?> findById(@PathVariable Long id) {
		return service.findById(id).map(record -> ResponseEntity.ok().body(record))
				.orElse(ResponseEntity.notFound().build());
	}
	
	@PostMapping
	@CacheEvict(value = "listCausaRaiz", allEntries = true)
	@ApiOperation("Cria uma nova causa raiz.")
	public ResponseEntity<?> create(@RequestBody @Valid CausaRaizDtoPost causaraiz) {
		return new ResponseEntity<CausaRaiz>(service.save(causaraiz.converter()), HttpStatus.CREATED);
	}

	@PutMapping(value = "/{id}")
	@CacheEvict(value = "listCausaRaiz", allEntries = true)
	@ApiOperation("Atualiza uma causa raiz pelo {id}.")
	public ResponseEntity<?> update(@PathVariable("id") Long id, @RequestBody @Valid CausaRaizDtoPut causaraiz) {
		return service.findById(id).map(record -> {
			record.setAtivo(causaraiz.getAtivo());
			record.setDescricao(causaraiz.getDescricao());
			CausaRaiz updated = service.save(record);
			return ResponseEntity.ok().body(updated);
		}).orElse(ResponseEntity.notFound().build());
	}

	@DeleteMapping(path = { "/{id}" })
	@CacheEvict(value = "listCausaRaiz", allEntries = true)
	@ApiOperation("Deleta a causa raiz pelo {id}.")
	public ResponseEntity<?> delete(@PathVariable Long id) {
		return service.findById(id).map(record -> {
			service.deleteById(id);
			return ResponseEntity.accepted().build();
		}).orElse(ResponseEntity.notFound().build());
	}
}
