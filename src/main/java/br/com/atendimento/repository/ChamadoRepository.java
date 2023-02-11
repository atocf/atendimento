package br.com.atendimento.repository;

import java.util.Date;
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

	List<Chamado> findByStatusintergrallAndSubmotivo_EquipeAndCnpjIsNotNull(String status_intergrall, String equipe);

	List<Chamado> findByStatusintergrallAndSubmotivo_EquipeAndSubmotivo_NomeAndCpfIsNotNull(String status_intergrall,
			String equipe, String submotivo);

	List<Chamado> findByStatusintergrallAndSubmotivo_EquipeAndSubmotivo_NomeAndCpfIsNotNullAndDataabertura(
			String status_intergrall, String equipe, String submotivo, Date data);

	List<Chamado> findByStatusintergrallAndStatus_Nome(String status_intergrall, String status);
	
	@Transactional
	@Modifying(clearAutomatically = true)
	@Query(value = "SELECT c.*\r\n"
			+ "FROM chamado c\r\n"
			+ "INNER JOIN sub_motivo sm on sm.id = c.idsubmotivo \r\n"
			+ "LEFT OUTER JOIN squad q on c.idsquad = q.id \r\n"
			+ "Where  c.statusintergrall = ?1 and sm.equipe = ?2\r\n"
			+ "and c.cpf in (SELECT c.cpf\r\n"
			+ "FROM chamado c\r\n"
			+ "INNER JOIN sub_motivo sm on sm.id = c.idsubmotivo \r\n"
			+ "Where  c.statusintergrall = ?1 and sm.equipe = ?2\r\n"
			+ "GROUP BY c.cpf HAVING COUNT(c.cpf) > 1)" , nativeQuery = true)
	List<Chamado> buscaListaOcorrenciaCpf(String status_intergrall, String equipe);
	
	@Transactional
	@Modifying(clearAutomatically = true)
	@Query(value = "SELECT c.*\r\n"
			+ "FROM bmg_atendimento.chamado c\r\n"
			+ "INNER JOIN bmg_atendimento.sub_motivo sm ON sm.id = c.idsubmotivo \r\n"
			+ "LEFT OUTER JOIN bmg_atendimento.squad q ON c.idsquad = q.id \r\n"
			+ "WHERE c.statusintergrall = ?1 AND sm.equipe = ?2 \r\n"
			+ "AND c.canalatendimento in ('RECLAME AQUI', 'CONSUMIDOR.GOV', 'BACEN', 'OUVIDORIA CLIENTE', 'PROCON', 'PROCON FONE')" , nativeQuery = true)
	List<Chamado> buscaListaOcorrenciaPrioritarias(String status_intergrall, String equipe); 
}