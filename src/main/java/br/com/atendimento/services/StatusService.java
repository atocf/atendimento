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

import br.com.atendimento.entity.Status;
import br.com.atendimento.exception.error.ErrorObject;
import br.com.atendimento.repository.StatusRepository;

@Service
public class StatusService {
	
	@Autowired
	private StatusRepository repository;

	private static final Logger log = LoggerFactory.getLogger(StatusService.class);

	public Page<Status> findAll(int page, int size, Boolean ativo) {
		log.info("Buscando o status ordenando pelo nome");
		Pageable paging = PageRequest.of(page, size, Sort.by("nome").ascending());
		return repository.findByAtivo(ativo, paging);
	}

	public Optional<Status> findById(Long id) {
		log.info("Buscando o status por id: {}", id);
		return repository.findById(id);
	}
	
	public Optional<Status> findByNome(String nome) {
		log.info("Buscando o status pelo nome: {}", nome);
		return repository.findByNome(nome);
	}
	
	public Status save(Status x) {
		log.info("Salvando ou Atualização o status {}", x.getNome());
		return repository.save(x);
	}

	public void deleteById(Long id) {
		log.info("Deletando o status por id: {}", id);
		repository.deleteById(id);
	}
	
	public ErrorObject returnError(Long id) {
		return new ErrorObject("Status selecionado não existe", "status_id", id);
	}
}