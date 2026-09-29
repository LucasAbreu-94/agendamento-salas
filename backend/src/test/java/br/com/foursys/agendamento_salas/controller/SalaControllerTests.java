package br.com.foursys.agendamento_salas.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDate;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

@ActiveProfiles("test")
@SpringBootTest
@AutoConfigureMockMvc
class SalaControllerTests {

	@Autowired
	private MockMvc mockMvc;

	@Test
	void deveRetornarTodasAsSalasParaUmaPessoa() throws Exception {
		mockMvc.perform(get("/api/salas/disponiveis")
						.param("data", LocalDate.now().plusDays(1).toString())
						.param("inicio", "14:00")
						.param("fim", "15:00")
						.param("pessoas", "1"))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.length()").value(8));
	}

	@Test
	void deveRetornarSomenteSalasComCapacidadeSuficiente() throws Exception {
		mockMvc.perform(get("/api/salas/disponiveis")
						.param("data", LocalDate.now().plusDays(1).toString())
						.param("inicio", "14:00")
						.param("fim", "15:00")
						.param("pessoas", "7"))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.length()").value(4));
	}

	@Test
	void deveRetornarListaVaziaQuandoNenhumaSalaAtende() throws Exception {
		mockMvc.perform(get("/api/salas/disponiveis")
						.param("data", LocalDate.now().plusDays(1).toString())
						.param("inicio", "14:00")
						.param("fim", "15:00")
						.param("pessoas", "99"))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.length()").value(0));
	}

	@Test
	void deveAceitarDuracaoEmLugarDoHorarioFinal() throws Exception {
		mockMvc.perform(get("/api/salas/disponiveis")
						.param("data", LocalDate.now().plusDays(1).toString())
						.param("inicio", "14:00")
						.param("duracao", "60")
						.param("pessoas", "4"))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.length()").value(8));
	}

	@Test
	void deveRejeitarDataNoPassado() throws Exception {
		mockMvc.perform(get("/api/salas/disponiveis")
						.param("data", LocalDate.now().minusDays(1).toString())
						.param("inicio", "14:00")
						.param("fim", "15:00")
						.param("pessoas", "4"))
				.andExpect(status().isBadRequest())
				.andExpect(jsonPath("$.mensagem").value("A data não pode estar no passado."));
	}

	@Test
	void deveRejeitarHorarioFinalAnteriorAoInicial() throws Exception {
		mockMvc.perform(get("/api/salas/disponiveis")
						.param("data", LocalDate.now().plusDays(1).toString())
						.param("inicio", "15:00")
						.param("fim", "14:00")
						.param("pessoas", "4"))
				.andExpect(status().isBadRequest())
				.andExpect(jsonPath("$.mensagem").value("O horário final deve ser posterior ao horário inicial."));
	}

	@Test
	void deveRejeitarBuscaSemFimEDuracao() throws Exception {
		mockMvc.perform(get("/api/salas/disponiveis")
						.param("data", LocalDate.now().plusDays(1).toString())
						.param("inicio", "14:00")
						.param("pessoas", "4"))
				.andExpect(status().isBadRequest())
				.andExpect(jsonPath("$.mensagem").value("Informe o horário final ou a duração da reserva."));
	}

	@Test
	void deveRejeitarPessoasMenorQueUm() throws Exception {
		mockMvc.perform(get("/api/salas/disponiveis")
						.param("data", LocalDate.now().plusDays(1).toString())
						.param("inicio", "14:00")
						.param("fim", "15:00")
						.param("pessoas", "0"))
				.andExpect(status().isBadRequest())
				.andExpect(jsonPath("$.mensagem").value("A quantidade de pessoas deve ser ao menos 1."));
	}

	@Test
	void deveRetornar404ParaRotaDesconhecida() throws Exception {
		mockMvc.perform(get("/api/salas/nao-existe")
						.param("data", LocalDate.now().plusDays(1).toString()))
				.andExpect(status().isNotFound())
				.andExpect(jsonPath("$.status").value(404));
	}

	@Test
	void deveRetornar400QuandoFaltarParametroObrigatorio() throws Exception {
		mockMvc.perform(get("/api/salas/disponiveis")
						.param("data", LocalDate.now().plusDays(1).toString())
						.param("inicio", "14:00")
						.param("fim", "15:00"))
				.andExpect(status().isBadRequest())
				.andExpect(jsonPath("$.status").value(400));
	}
}
