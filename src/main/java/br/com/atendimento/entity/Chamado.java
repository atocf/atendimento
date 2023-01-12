package br.com.atendimento.entity;

import java.util.Date;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
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
@Table(indexes = {
		@Index(name = "index_chamado_status_integrall", columnList = "statusintergrall", unique = false),
		@Index(name = "index_chamado_data_abertura", columnList = "dataabertura", unique = false),
		@Index(name = "index_chamado_data_vencimento", columnList = "datavencimento", unique = false),
		@Index(name = "index_chamado_cpf", columnList = "cpf", unique = false),
		@Index(name = "index_chamado_status_senha", columnList = "statussenha", unique = false)		
	})
public class Chamado {

	@Id
	@Column(nullable = false, unique = true)
	private Long protocolo;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "idstatus")
	private Status status;

	@Column(nullable = false)
	private String statusintergrall;

	@Temporal(TemporalType.TIMESTAMP)
	@JsonFormat(pattern = "dd-MM-yyyy HH:mm")
	@Column(nullable = true, columnDefinition = "TIMESTAMP")
	private Date datastatus;

	@Column(nullable = false, unique = true)
	private Long ocorrencia;

	@Column(nullable = false)
	private String canalatendimento;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "idsubmotivo")
	private SubMotivo submotivo;

	@Column(nullable = false)
	private Integer reabertura;

	@Column(nullable = true)
	private Integer qtdresolucao;

	@Temporal(TemporalType.TIMESTAMP)
	@JsonFormat(pattern = "dd-MM-yyyy HH:mm")
	@Column(nullable = false, columnDefinition = "TIMESTAMP")
	private Date dataabertura;

	@Temporal(TemporalType.TIMESTAMP)
	@JsonFormat(pattern = "dd-MM-yyyy HH:mm")
	@Column(nullable = false, columnDefinition = "TIMESTAMP")
	private Date datavencimento;

	@Temporal(TemporalType.TIMESTAMP)
	@JsonFormat(pattern = "dd-MM-yyyy HH:mm")
	@Column(nullable = true, columnDefinition = "TIMESTAMP")
	private Date dataconclusao;

	@Column(nullable = true, length = 10000)
	private String descricao;

	@Column(nullable = true, length = 11)
	private String cpf;

	@Column(nullable = true, length = 14)
	private String cnpj;

	@Column(nullable = false)
	private String nome;

	@Column(nullable = true)
	private String email;

	@Column(nullable = true)
	private String telefone;

	@Column(nullable = true, columnDefinition = "tinyint(1)")
	private Boolean statussms;

	@Column(nullable = true)
	private String escopo;

	@Column(nullable = true)
	private String statussenha;

	@Temporal(TemporalType.TIMESTAMP)
	@JsonFormat(pattern = "dd-MM-yyyy HH:mm")
	@Column(nullable = true, columnDefinition = "TIMESTAMP")
	private Date dataatualizacaocadastral;

	@OneToMany(fetch = FetchType.EAGER)
	@JoinColumn(name = "idchamado")
	private Set<Conta> conta;
	
	@OneToMany(fetch = FetchType.EAGER)
	@JoinColumn(name = "idchamado")
	private Set<Cartao> cartao;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "idanalista")
	private Analista analista;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "idsquad")
	private Squad squad;

	@Column(nullable = true)
	private String card;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "idcausaraiz")
	private CausaRaiz causaraiz;

	@Column(nullable = true, length = 1000)
	private String observacao;

}
