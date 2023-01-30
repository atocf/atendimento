package br.com.atendimento.services;

import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.atendimento.entity.Chamado;
import br.com.atendimento.exception.error.ErrorObject;
import br.com.atendimento.repository.ChamadoRepository;

@Service
public class ChamadoService {

	@Autowired
	private ChamadoRepository repository;

	private static final Logger log = LoggerFactory.getLogger(ChamadoService.class);

	public Optional<Chamado> findById(Long id) {
		log.info("Buscando o chamado por id: {}", id);
		return repository.findById(id);
	}

	public Chamado save(Chamado x) {
		log.info("Salvando ou Atualização o chamado {}", x.getProtocolo());
		return repository.save(x);
	}

	public void deleteById(Long id) {
		log.info("Deletando o chamado por id: {}", id);
		repository.deleteById(id);
	}

	public ErrorObject returnError(Long id) {
		return new ErrorObject("Chamado selecionado não existe", "chamado_id", id);
	}

	public List<Chamado> findByStatusintergrallAndSubmotivo_SincronismoAndSubmotivo_Equipe(String status_intergrall,
			boolean sincronismo, String equipe) {
		log.info("Buscar lista de chamados para sincronismo");
		return repository.findByStatusintergrallAndSubmotivo_SincronismoAndSubmotivo_Equipe(status_intergrall,
				sincronismo, equipe);
	}

	public List<Chamado> findByStatusintergrallAndSubmotivo_Equipe(String status_intergrall, String equipe) {
		log.info("Buscar lista de chamados para atendimento");
		return repository.findByStatusintergrallAndSubmotivo_Equipe(status_intergrall, equipe);
	}

	public List<Chamado> findByStatusintergrallAndSubmotivo_Sincronismo(String status_intergrall, boolean sincronismo) {
		log.info("Buscar lista de chamados para sincronismo");
		return repository.findByStatusintergrallAndSubmotivo_Sincronismo(status_intergrall, sincronismo);
	}

	public List<Chamado> findByStatusintergrallAndSubmotivo_EquipeAndSubmotivo_Pix(String status_intergrall,
			String equipe, boolean pix) {
		log.info("Buscar lista de chamados para atendimento");
		return repository.findByStatusintergrallAndSubmotivo_EquipeAndSubmotivo_Pix(status_intergrall, equipe, pix);
	}

	public void updateAllFinalizado(String equipe) {
		repository.updateAllFinalizado(equipe);	
	}

	public List<Chamado> findByStatusintergrallAndSubmotivo_EquipeAndCnpjIsNotNull(String status_intergrall, String equipe) {
		log.info("Consultar protocolos que tem que ser devolvidos por serem CNPJ");
		return repository.findByStatusintergrallAndSubmotivo_EquipeAndCnpjIsNotNull(status_intergrall, equipe);
	}
}