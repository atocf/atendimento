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

import br.com.atendimento.entity.CausaRaiz;
import br.com.atendimento.exception.error.ErrorObject;
import br.com.atendimento.repository.CausaRaizRepository;

@Service
public class CausaRaizService {
	
	@Autowired
	private CausaRaizRepository repository;

	private static final Logger log = LoggerFactory.getLogger(CausaRaizService.class);

	public Page<CausaRaiz> findAll(int page, int size, Boolean ativo) {
		log.debug("Buscando a causa raiz ordenando pelo nome");
		Pageable paging = PageRequest.of(page, size, Sort.by("nome").ascending());
		return repository.findByAtivo(ativo, paging);
	}

	public Optional<CausaRaiz> findById(Long id) {
		log.debug("Buscando a causa raiz por id: {}", id);
		return repository.findById(id);
	}
	
	public Optional<CausaRaiz> findByNome(String nome) {
		log.debug("Buscando a causa raiz pelo nome: {}", nome);
		return repository.findByNome(nome);
	}
	
	public CausaRaiz save(CausaRaiz x) {
		log.debug("Salvando ou Atualização a causa raiz {}", x.getNome());
		return repository.save(x);
	}

	public void deleteById(Long id) {
		log.debug("Deletando a causa raiz por id: {}", id);
		repository.deleteById(id);
	}
	
	public ErrorObject returnError(Long id) {
		return new ErrorObject("Causa Raiz selecionada não existe", "causaraiz_id", id);
	}

	public CausaRaiz findCausaRaiz(String nome) {
		Optional<CausaRaiz> causaRaiz = repository.findByNome(nome);
		if(causaRaiz.isPresent()) {
			return causaRaiz.get();
		} 		
		return repository.save(new CausaRaiz(nome, "Definir"));
	}
	
	
}