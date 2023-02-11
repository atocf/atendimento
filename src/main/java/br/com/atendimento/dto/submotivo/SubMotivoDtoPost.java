package br.com.atendimento.dto.submotivo;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import br.com.atendimento.entity.Analista;
import br.com.atendimento.entity.Kibana;
import br.com.atendimento.entity.SubMotivo;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class SubMotivoDtoPost {
	
	@NotNull
	private Long analista_id;
	
	@NotNull
	@NotBlank
	@NotEmpty
	private String produto;

	@NotNull
	@NotBlank
	@NotEmpty
	private String tipopublico;

	@NotNull
	@NotBlank
	@NotEmpty
	private String variedadeproduto;

	@NotNull
	@NotBlank
	@NotEmpty
	private String motivo;
	
	@NotNull
	@NotBlank
	@NotEmpty
	private String nome;
	
	@NotNull
	@NotBlank
	@NotEmpty
	private String equipe;
	
	@NotNull
	private Long kibana_id;
	
	public SubMotivo converter(Analista analista, Kibana kibana) {		
		return new SubMotivo(analista, produto, tipopublico, variedadeproduto, motivo, nome, equipe, kibana);
	}	
}
