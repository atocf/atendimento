package br.com.atendimento.dto.submotivo;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class SubMotivoDtoPut {
	
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
	private String equipe;
	
	@NotNull
	@NotBlank
	@NotEmpty
	private String status;
	
	@NotNull
	private Boolean sincronismo;
	
	@NotNull
	private Boolean cdtp004;
	
	@NotNull
	private Boolean dgp001;
	
	@NotNull
	private Boolean dgp041;
	
	@NotNull
	private Boolean imp001;
	
	@NotNull
	private Boolean imp003;
}
