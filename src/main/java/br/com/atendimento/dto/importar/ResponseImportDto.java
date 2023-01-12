package br.com.atendimento.dto.importar;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class ResponseImportDto {
	
	private boolean status;
	private int total_enviados; 
	private int total_importados;
	private String msg;
	private List<ErrosImportDto> erros;
}