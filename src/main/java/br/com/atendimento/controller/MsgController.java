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

import br.com.atendimento.dto.msg.MsgDtoPost;
import br.com.atendimento.dto.msg.MsgDtoPut;
import br.com.atendimento.entity.Msg;
import br.com.atendimento.services.MsgService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping("/msg")
@Api(tags = "Mensagem", description = "Endpoint´s incluir, atualizar, deletar ou buscar uma mensagem.")
public class MsgController {
	
	@Autowired
	private MsgService service;

	@GetMapping
	@Cacheable(value = "listMsg")
	@ApiOperation("Retorna a lista de Mensagens.")
	public Page<Msg> findAll(@RequestParam(value = "page", required = false, defaultValue = "0") int page,
			@RequestParam(value = "size", required = false, defaultValue = "50") int size,
			@RequestParam(value = "ativo", required = true, defaultValue = "true") Boolean ativo) {
		return service.findAll(page, size, ativo);
	}

	@GetMapping(path = { "/{id}" })
	@ApiOperation("Retorna a mensagem pelo {id}.")
	public ResponseEntity<?> findById(@PathVariable Long id) {
		return service.findById(id).map(record -> ResponseEntity.ok().body(record))
				.orElse(ResponseEntity.notFound().build());
	}
	
	@PostMapping
	@CacheEvict(value = "listMsg", allEntries = true)
	@ApiOperation("Cria uma nova mensagem.")
	public ResponseEntity<?> create(@RequestBody @Valid MsgDtoPost status) {
		return new ResponseEntity<Msg>(service.save(status.converter()), HttpStatus.CREATED);
	}

	@PutMapping(value = "/{id}")
	@CacheEvict(value = "listMsg", allEntries = true)
	@ApiOperation("Atualiza uma mensagem pelo {id}.")
	public ResponseEntity<?> update(@PathVariable("id") Long id, @RequestBody @Valid MsgDtoPut status) {
		return service.findById(id).map(record -> {
			record.setAtivo(status.getAtivo());
			record.setDescricao(status.getDescricao());
			Msg updated = service.save(record);
			return ResponseEntity.ok().body(updated);
		}).orElse(ResponseEntity.notFound().build());
	}

	@DeleteMapping(path = { "/{id}" })
	@CacheEvict(value = "listMsg", allEntries = true)
	@ApiOperation("Deleta a mensagem pelo {id}.")
	public ResponseEntity<?> delete(@PathVariable Long id) {
		return service.findById(id).map(record -> {
			service.deleteById(id);
			return ResponseEntity.accepted().build();
		}).orElse(ResponseEntity.notFound().build());
	}
}
