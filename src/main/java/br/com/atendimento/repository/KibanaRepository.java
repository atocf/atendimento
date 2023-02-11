package br.com.atendimento.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import br.com.atendimento.entity.Kibana;

public interface KibanaRepository extends JpaRepository<Kibana, Long> {

	Page<Kibana> findByAtivo(Boolean ativo, Pageable paging);
}