package br.com.atendimento.services;

import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import br.com.atendimento.entity.Analista;
import br.com.atendimento.exception.error.ErrorObject;
import br.com.atendimento.repository.AnalistaRepository;

@Service
public class AnalistaService {
	
	@Autowired
	private AnalistaRepository repository;

	private static final Logger log = LoggerFactory.getLogger(AnalistaService.class);

	public Page<Analista> findAll(int page, int size, Boolean ativo) {
		log.info("Buscando a/o analista ordenando pelo nome");
		Pageable paging = PageRequest.of(page, size, Sort.by("nome").ascending());
		return repository.findByAtivo(ativo, paging);
	}

	public Optional<Analista> findById(Long id) {
		log.info("Buscando a/o analista por id: {}", id);
		return repository.findById(id);
	}
	
	public Optional<Analista> findByNome(String nome) {
		log.info("Buscando a/o analista pelo nome: {}", nome);
		return repository.findByNome(nome);
	}
	

	public List<Analista> findByNomeLike(String nome) {
		log.info("Buscando a/o analista pelo texto: {}", nome);
		return repository.findByNomeLike(nome);
	}
	
	public Analista save(Analista x) {
		log.info("Salvando ou Atualização a/o analista {}", x.getNome());
		return repository.save(x);
	}

	public void deleteById(Long id) {
		log.info("Deletando a/o Analista por id: {}", id);
		repository.deleteById(id);
	}
	
	public ErrorObject returnError(Long id) {
		return new ErrorObject("Analista selecionada/o não existe", "analista_id", id);
	}
	
	public Analista findAnalista(String nome) {
		if(nome != null) {
			List<Analista> analista = findByNomeLike("%"+nome.split(" ")[0]+"%");
			if(analista.size() > 0) {
				return analista.get(0);
			} 
		}
		return findById(1L).get();
	}
}