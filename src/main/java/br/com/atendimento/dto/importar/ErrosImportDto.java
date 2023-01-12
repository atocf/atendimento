package br.com.atendimento.dto.importar;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class ErrosImportDto {
	
	private String protocolo;
	private String erro;	
}