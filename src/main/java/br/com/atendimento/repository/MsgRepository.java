package br.com.atendimento.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import br.com.atendimento.entity.Msg;

public interface MsgRepository extends JpaRepository<Msg, Long> {

	Page<Msg> findByAtivo(Boolean ativo, Pageable paging);
}