package br.com.atendimento.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import br.com.atendimento.entity.Analista;

public interface AnalistaRepository extends JpaRepository<Analista, Long> {

	Page<Analista> findByAtivo(Boolean ativo, Pageable paging);

	Optional<Analista> findByNome(String nome);

	List<Analista> findByNomeLike(String nome);
}