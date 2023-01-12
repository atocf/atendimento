package br.com.atendimento.entity;

import java.time.Instant;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
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
public class Status {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Temporal(TemporalType.TIMESTAMP)
	@JsonFormat(pattern = "dd-MM-yyyy HH:mm")
	@Column(nullable =  false, columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP") 
	private Date datacriacao = Date.from(Instant.now());
	
	@Column(nullable = false, columnDefinition = "tinyint(1) DEFAULT 1")
	private Boolean ativo = true;
	
	@Column(nullable = false, unique = true)
	private String nome;
	
	public Status(String nome) {
		super();
		this.nome = nome;
	}
}
