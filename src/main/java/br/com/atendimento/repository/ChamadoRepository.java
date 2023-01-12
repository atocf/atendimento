package br.com.atendimento.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.atendimento.entity.Chamado;

public interface ChamadoRepository extends JpaRepository<Chamado, Long> {

	List<Chamado> findByStatusintergrallAndSubmotivo_SincronismoAndSubmotivo_Equipe(String status_intergrall,
			boolean sincronismo, String equipe);

	List<Chamado> findByStatusintergrallAndSubmotivo_Equipe(String status_intergrall, String equipe);

	List<Chamado> findByStatusintergrallAndSubmotivo_Sincronismo(String status_intergrall, boolean sincronismo);
}