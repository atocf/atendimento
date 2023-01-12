package br.com.atendimento.repository;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import br.com.atendimento.entity.CausaRaiz;

public interface CausaRaizRepository extends JpaRepository<CausaRaiz, Long> {

	Page<CausaRaiz> findByAtivo(Boolean ativo, Pageable paging);

	Optional<CausaRaiz> findByNome(String nome);
}