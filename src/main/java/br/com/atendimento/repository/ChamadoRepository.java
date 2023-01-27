package br.com.atendimento.repository;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import br.com.atendimento.entity.Chamado;

public interface ChamadoRepository extends JpaRepository<Chamado, Long> {

	List<Chamado> findByStatusintergrallAndSubmotivo_SincronismoAndSubmotivo_Equipe(String status_intergrall,
			boolean sincronismo, String equipe);

	List<Chamado> findByStatusintergrallAndSubmotivo_Equipe(String status_intergrall, String equipe);

	List<Chamado> findByStatusintergrallAndSubmotivo_Sincronismo(String status_intergrall, boolean sincronismo);

	List<Chamado> findByStatusintergrallAndSubmotivo_EquipeAndSubmotivo_Pix(String status_intergrall, String equipe,
			boolean pix);

	@Transactional
	@Modifying(clearAutomatically = true)
	@Query(value = "UPDATE chamado set statusintergrall = 'Finalizado' WHERE ocorrencia in (SELECT o FROM ( SELECT c.ocorrencia as o FROM chamado c INNER JOIN sub_motivo sm on sm.id = c.idsubmotivo Where sm.equipe = ?1 order by c.ocorrencia) as s)", nativeQuery = true)
	int updateAllFinalizado(String equipe);
}