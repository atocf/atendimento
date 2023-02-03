package br.com.atendimento.dto.analisar;

import java.util.List;

import javax.validation.constraints.NotNull;

import br.com.atendimento.entity.SubMotivo;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class ResponseAnalisarMassaDto {
	
	 @NotNull
	 private String data; 
	 
	 @NotNull
	 private List<SubMotivo> subMotivos;
	 
	 @NotNull
	 private Long analista;
	 
	 @NotNull
	 private Long causa_raiz;
	 
	 @NotNull
	 private Long msg;
}

