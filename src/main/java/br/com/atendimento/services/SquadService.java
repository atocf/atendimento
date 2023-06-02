package br.com.atendimento.services;

import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import br.com.atendimento.entity.Squad;
import br.com.atendimento.exception.error.ErrorObject;
import br.com.atendimento.repository.SquadRepository;

@Service
public class SquadService {
	
	@Autowired
	private SquadRepository repository;

	private static final Logger log = LoggerFactory.getLogger(SquadService.class);

	public Page<Squad> findAll(int page, int size, Boolean ativo) {
		log.debug("Buscando a squad ordenando pelo nome");
		Pageable paging = PageRequest.of(page, size, Sort.by("nome").ascending());
		return repository.findByAtivo(ativo, paging);
	}

	public Optional<Squad> findById(Long id) {
		log.debug("Buscando a squad por id: {}", id);
		return repository.findById(id);
	}

	public Optional<Squad> findByNome(String nome) {
		log.debug("Buscando a squad pelo nome: {}", nome);
		return repository.findByNome(nome);
	}
	
	public Squad save(Squad x) {
		log.debug("Salvando ou Atualização a squad {}", x.getNome());
		return repository.save(x);
	}

	public void deleteById(Long id) {
		log.debug("Deletando a squad por id: {}", id);
		repository.deleteById(id);
	}
	
	public ErrorObject returnError(Long id) {
		return new ErrorObject("Squad selecionado não existe", "squad_id", id);
	}

}