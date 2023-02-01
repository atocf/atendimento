package br.com.atendimento.dto.dgp018;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class Dgp018StatusDto {
	
	private String status;
	private String cpf;
	private String clienteComSenhaCadastrada; 
}
