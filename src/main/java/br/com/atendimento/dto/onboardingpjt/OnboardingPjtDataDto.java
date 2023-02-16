package br.com.atendimento.dto.onboardingpjt;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class OnboardingPjtDataDto {
	
	private String statusTicket;
	private OnboardingPjtDataEmpresaDto dadosEmpresariais;
}
