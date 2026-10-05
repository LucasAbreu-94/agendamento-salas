package br.com.foursys.agendamento_salas.config;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.options;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
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
class CorsTests {

	@Autowired
	private MockMvc mockMvc;

	@Test
	void devePermitirPreflightDaOrigemDoFrontend() throws Exception {
		mockMvc.perform(options("/api/salas/disponiveis")
						.header("Origin", "http://localhost:4200")
						.header("Access-Control-Request-Method", "GET")
						.header("Access-Control-Request-Headers", "authorization"))
				.andExpect(status().isOk())
				.andExpect(header().string("Access-Control-Allow-Origin", "http://localhost:4200"))
				.andExpect(header().string("Access-Control-Allow-Methods", containsString("GET")))
				.andExpect(header().exists("Access-Control-Allow-Headers"));
	}

	@Test
	void deveRejeitarPreflightDeOrigemNaoPermitida() throws Exception {
		mockMvc.perform(options("/api/salas/disponiveis")
						.header("Origin", "http://evil.example")
						.header("Access-Control-Request-Method", "GET"))
				.andExpect(status().isForbidden())
				.andExpect(header().doesNotExist("Access-Control-Allow-Origin"));
	}

	@Test
	void deveLiberarOrigemEmRequisicaoSimples() throws Exception {
		mockMvc.perform(get("/api/salas/disponiveis")
						.param("data", LocalDate.now().plusDays(1).toString())
						.param("inicio", "14:00")
						.param("fim", "15:00")
						.param("pessoas", "4")
						.header("Origin", "http://localhost:4200"))
				.andExpect(status().isOk())
				.andExpect(header().string("Access-Control-Allow-Origin", "http://localhost:4200"));
	}
}
