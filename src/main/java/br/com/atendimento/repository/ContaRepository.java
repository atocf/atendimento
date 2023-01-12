package br.com.atendimento.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.atendimento.entity.Conta;

public interface ContaRepository extends JpaRepository<Conta, Long> {

}