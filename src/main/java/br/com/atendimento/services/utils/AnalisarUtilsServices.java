package br.com.atendimento.services.utils;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.atendimento.entity.Chamado;
import br.com.atendimento.services.AnalistaService;
import br.com.atendimento.services.CausaRaizService;
import br.com.atendimento.services.ChamadoService;
import br.com.atendimento.services.Dgp018Service;
import br.com.atendimento.services.Dgp180Service;
import br.com.atendimento.services.Imp013Service;
import br.com.atendimento.services.MsgService;
import br.com.atendimento.services.StatusService;

@Service
public class AnalisarUtilsServices {

	@Autowired
	private ChamadoService chamadoService;

	@Autowired
	private StatusService statusService;

	@Autowired
	private AnalistaService analistaService;

	@Autowired
	private CausaRaizService causaRaizService;

	@Autowired
	private MsgService msgService;
	
	@Autowired
	private Dgp018Service dgp018Service;

	@Autowired
	private Dgp180Service dgp180Service;

	@Autowired
	private Imp013Service imp013Service;

	public void validarStatusChamados(String statusOld, String statusNew) {
		List<Chamado> list = chamadoService.findByStatusintergrallAndStatus_Nome("Finalizado", statusOld);

		if (list.size() > 0) {
			for (Chamado c : list) {
				c.setStatus(statusService.findByNome(statusNew).get());
				chamadoService.save(c);
			}
		}
	}

	public void atualizarChamado(Chamado chamado, String status, Long analista, Long cauzaRaiz, Long msg,
			Boolean analisado, Boolean massa) {
		if (status != null) {
			chamado.setStatus(statusService.findByNome(status).get());
		}
		if (analista != null) {
			chamado.setAnalista(analistaService.findById(analista).get());
		}
		chamado.setCausaraiz(causaRaizService.findById(cauzaRaiz).get());
		chamado.setDatastatus(new Date());
		if (msg != null) {
			chamado.setMsg(msgService.findById(msg).get());
		}
		chamado.setAnalisado(analisado);
		chamado.setMassa(massa);
		chamadoService.save(chamado);
	}
	
	public void resetOnboarding(Chamado chamado, Long cauzaRaiz, Boolean analisado, Boolean massa) {
		dgp018Service.limpezaCadastro(chamado.getCpf());
		dgp180Service.deleteRedis(chamado.getCpf());
		imp013Service.inativarPinEletronico(chamado.getCpf());
		atualizarChamado(chamado, "FECHAR", null, cauzaRaiz, 4L, analisado, massa);
	}
}
