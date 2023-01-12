package br.com.atendimento.repository;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import br.com.atendimento.entity.SubMotivo;

public interface SubMotivoRepository extends JpaRepository<SubMotivo, Long> {

	Optional<SubMotivo> findByNome(String nome);

	Page<SubMotivo> findByStatus(String status, Pageable paging);

	Optional<SubMotivo> findByNomeAndEquipe(String nome, String equipe);	

}