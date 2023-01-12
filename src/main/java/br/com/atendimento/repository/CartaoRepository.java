package br.com.atendimento.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.atendimento.entity.Cartao;

public interface CartaoRepository extends JpaRepository<Cartao, Long> {

}