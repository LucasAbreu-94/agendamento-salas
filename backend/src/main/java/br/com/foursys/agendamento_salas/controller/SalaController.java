package br.com.foursys.agendamento_salas.controller;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import br.com.foursys.agendamento_salas.domain.Sala;
import br.com.foursys.agendamento_salas.service.SalaBuscaService;

@RestController
@RequestMapping("/api/salas")
public class SalaController {

	private final SalaBuscaService salaBuscaService;

	public SalaController(SalaBuscaService salaBuscaService) {
		this.salaBuscaService = salaBuscaService;
	}

	@GetMapping("/disponiveis")
	public List<Sala> disponiveis(
			@RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate data,
			@RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.TIME) LocalTime inicio,
			@RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.TIME) LocalTime fim,
			@RequestParam(required = false) Integer duracao,
			@RequestParam Integer pessoas) {
		return salaBuscaService.buscar(data, inicio, fim, duracao, pessoas);
	}
}
