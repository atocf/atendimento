package br.com.atendimento.dto.importar;

import java.util.List;

import br.com.atendimento.dto.analisar.ResponseAnalisarDto;
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
	private int total_Pf;
	private int total_Pj;
	private int total_Pf_Pix;
	private int total_Pf_Preferencial;
	private int total_BMG_Empresas; 
	private int total_BackOffice_Digital;	
	private String msg;
	private List<ErrosImportDto> erros;
	private ResponseAnalisarDto analisados;
}