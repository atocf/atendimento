package br.com.atendimento.services;

import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.atendimento.entity.Cartao;
import br.com.atendimento.exception.error.ErrorObject;
import br.com.atendimento.repository.CartaoRepository;

@Service
public class CartaoService {
	
	@Autowired
	private CartaoRepository repository;

	private static final Logger log = LoggerFactory.getLogger(CartaoService.class);

	public Optional<Cartao> findById(Long id) {
		log.info("Buscando o cartao por id: {}", id);
		return repository.findById(id);
	}
	
	public Cartao save(Cartao x) {
		log.info("Salvando ou Atualização o cartao {}", x.getNumerocartao());
		return repository.save(x);
	}

	public void deleteById(Long id) {
		log.info("Deletando o cartao por id: {}", id);
		repository.deleteById(id);
	}
	
	public ErrorObject returnError(Long id) {
		return new ErrorObject("Cartao selecionado não existe", "cartao_id", id);
	}

	public List<Cartao> findByChamado_Protocolo(Long protocolo) {
		log.info("Consulta lista de cartões pelo protocolo: {}", protocolo);
		return repository.findByChamado_Protocolo(protocolo);
	}

	public List<Cartao> findByChamado_Statusintergrall(String status_intergrall) {
		return repository.findByChamado_Statusintergrall(status_intergrall);
	}
}