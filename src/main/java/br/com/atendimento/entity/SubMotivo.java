package br.com.atendimento.entity;

import java.time.Instant;
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
public class SubMotivo {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Temporal(TemporalType.TIMESTAMP)
	@JsonFormat(pattern = "dd-MM-yyyy HH:mm")
	@Column(nullable =  false, columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP") 
	private Date datacriacao = Date.from(Instant.now());
	
	@Column(nullable = false)
	private String status = "Ativo";
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "analista_id")
	private Analista analista;
	
	@Column(nullable = false)
	private String produto;

	@Column(nullable = false)
	private String tipopublico;

	@Column(nullable = false)
	private String variedadeproduto;

	@Column(nullable = false)
	private String motivo;
	
	@Column(nullable = false)
	private String equipe;
	
	@Column(nullable = false)
	private String nome;
	
	@Column(nullable = false, columnDefinition = "tinyint(1) DEFAULT 0")
	private Boolean sincronismo = false;
	
	@Column(nullable = false, columnDefinition = "tinyint(1) DEFAULT 0")
	private Boolean pix = false;
	
	//Consulta dados cartão
	@Column(nullable = false, columnDefinition = "tinyint(1) DEFAULT 0")
	private Boolean cdtp004 = false;
	
	//Consulta dados cliente
	@Column(nullable = false, columnDefinition = "tinyint(1) DEFAULT 0")
	private Boolean dgp001 = false;
	
	//Consulta scopo
	@Column(nullable = false, columnDefinition = "tinyint(1) DEFAULT 0")
	private Boolean dgp041 = false;
	
	//Consulta dados conta
	@Column(nullable = false, columnDefinition = "tinyint(1) DEFAULT 1")
	private Boolean imp001 = true;
	
	//Consulta status senha
	@Column(nullable = false, columnDefinition = "tinyint(1) DEFAULT 1")
	private Boolean imp003 = true;
	
	public SubMotivo(Analista analista, String produto, String tipopublico, String variedadeproduto, String motivo, String nome, String equipe) {
		super();
		this.analista = analista;
		this.produto = produto;
		this.tipopublico = tipopublico;
		this.variedadeproduto = variedadeproduto;
		this.motivo = motivo;
		this.nome = nome;
		this.equipe = equipe;
	}
}
