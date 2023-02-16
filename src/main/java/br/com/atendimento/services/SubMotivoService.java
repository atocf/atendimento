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

import br.com.atendimento.entity.Analista;
import br.com.atendimento.entity.SubMotivo;
import br.com.atendimento.repository.SubMotivoRepository;

@Service
public class SubMotivoService {

	@Autowired
	private SubMotivoRepository repository;

	@Autowired
	private AnalistaService analistaService;

	private static final Logger log = LoggerFactory.getLogger(SubMotivoService.class);

	public Page<SubMotivo> findAll(int page, int size, String status) {
		log.info("Buscando o submotivo ordenando pelo nome");
		Pageable paging = PageRequest.of(page, size, Sort.by("nome").ascending());
		return repository.findByStatus(status, paging);
	}

	public Optional<SubMotivo> findById(Long id) {
		log.info("Buscando o submotivo por id: {}", id);
		return repository.findById(id);
	}

	public Optional<SubMotivo> findByNome(String nome) {
		log.info("Buscando o submotivo pelo nome: {}", nome);
		return repository.findByNome(nome);
	}

	public SubMotivo save(SubMotivo x) {
		log.info("Salvando ou Atualização o submotivo {}", x.getNome());
		return repository.save(x);
	}

	public void deleteById(Long id) {
		log.info("Deletando o submotivo por id: {}", id);
		repository.deleteById(id);
	}

	public SubMotivo findSubMotivo(String produto, String tipopublico, String variedadeproduto, String motivo,
			String nome, String equipe, String analista, String status_integrall, String canal) {
		Optional<SubMotivo> subMotivo = repository.findByNomeAndEquipe(nome, equipe);
		Analista a = analistaService.findAnalista(analista, canal);
		if (subMotivo.isPresent()) {
			if (analista != null) {
				subMotivo.get().setAnalista(a);
			} 		
			return subMotivo.get();
		}
		return save(new SubMotivo(a, produto, tipopublico, variedadeproduto, motivo, nome, equipe, null));
	}
}