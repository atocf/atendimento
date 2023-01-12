package br.com.atendimento.repository;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import br.com.atendimento.entity.Status;

public interface StatusRepository extends JpaRepository<Status, Long> {

	Page<Status> findByAtivo(Boolean ativo, Pageable paging);

	Optional<Status> findByNome(String nome);
}