package br.com.atendimento.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
public class Conta {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "idchamado")
	private Chamado chamado;
	
	@Column(nullable = false)
	private Integer agencia;
	
	@Column(nullable = false)
	private String nomeagencia;
	
	@Column(nullable = false)
	private Integer numeroconta;
	
	@Column(nullable = false)
	private Integer tipoconta;
	
	@Column(nullable = false)
	private String descricaotipoconta;
	
	@Column(nullable = false)
	private String situacao;
	
	@Column(nullable = false)
	private String descricaosituacao;
	
	@Temporal(TemporalType.TIMESTAMP)
	@JsonFormat(pattern = "dd-MM-yyyy HH:mm")
	@Column(nullable =  false, columnDefinition = "TIMESTAMP") 
	private Date abertura;
	
}
