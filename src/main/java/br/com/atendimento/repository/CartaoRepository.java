package br.com.atendimento.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.atendimento.entity.Cartao;

public interface CartaoRepository extends JpaRepository<Cartao, Long> {

	List<Cartao> findByChamado_Protocolo(Long protocolo);

}