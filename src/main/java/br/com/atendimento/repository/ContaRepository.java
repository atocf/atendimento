package br.com.atendimento.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.atendimento.entity.Conta;

public interface ContaRepository extends JpaRepository<Conta, Long> {

	List<Conta> findByChamado_Ocorrencia(Long ocorrencia);

}