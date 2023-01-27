package br.com.atendimento.services;

import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.atendimento.entity.Conta;
import br.com.atendimento.exception.error.ErrorObject;
import br.com.atendimento.repository.ContaRepository;

@Service
public class ContaService {
	
	@Autowired
	private ContaRepository repository;

	private static final Logger log = LoggerFactory.getLogger(ContaService.class);

	public Optional<Conta> findById(Long id) {
		log.info("Buscando a conta por id: {}", id);
		return repository.findById(id);
	}
	
	public Conta save(Conta x) {
		log.info("Salvando ou Atualização a conta {}", x.getNumeroconta());
		return repository.save(x);
	}

	public void deleteById(Long id) {
		log.info("Deletando a conta por id: {}", id);
		repository.deleteById(id);
	}
	
	public ErrorObject returnError(Long id) {
		return new ErrorObject("Conta selecionado não existe", "conta_id", id);
	}

	public List<Conta> findByChamado_Ocorrencia(Long ocorrencia) {
		log.info("Consulta lista de contas pelo ocorrencia: {}", ocorrencia);
		return repository.findByChamado_Ocorrencia(ocorrencia);
	}

}