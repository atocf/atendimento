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
public class Cartao {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "idchamado")
	private Chamado chamado;
	
	@Column(nullable = false)
	private String numerocartao;
	
	@Column(nullable = false)
	private String nomeimpresso;
	
	@Temporal(TemporalType.TIMESTAMP)
	@JsonFormat(pattern = "dd-MM-yyyy HH:mm")
	@Column(nullable =  false, columnDefinition = "TIMESTAMP") 
	private Date datavalidade;
	
	@Column(nullable = false)
	private String descricaostatuscartao;
	
	@Column(nullable = false, columnDefinition = "tinyint(1)")
	private Boolean cartaovirtual;
	
	@Column(nullable = false, columnDefinition = "tinyint(1)")
	private Boolean virtualizavel;
	
	@Column(nullable = false)
	private String descricaotipocartao;
	
	@Column(nullable = false) 
	private Integer numerobin;
}
