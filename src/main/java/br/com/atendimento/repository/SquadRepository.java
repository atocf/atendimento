package br.com.atendimento.repository;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import br.com.atendimento.entity.Squad;

public interface SquadRepository extends JpaRepository<Squad, Long> {

	Page<Squad> findByAtivo(Boolean ativo, Pageable paging);

	Optional<Squad> findByNome(String nome);
}