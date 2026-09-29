package br.com.foursys.agendamento_salas.service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import org.springframework.stereotype.Service;

import br.com.foursys.agendamento_salas.domain.Sala;
import br.com.foursys.agendamento_salas.exception.ValidacaoBuscaException;

@Service
public class SalaBuscaService {

	private static final List<Sala> SALAS = List.of(
			new Sala("focus", "Sala Focus", "Tamboré", 8),
			new Sala("comp-01", "Sala Comp 01", "Tamboré", 6),
			new Sala("comp-02", "Sala Comp 02", "Tamboré", 6),
			new Sala("comp-03", "Sala Comp 03", "Tamboré", 4),
			new Sala("comp-04", "Sala Comp 04", "Tamboré", 4),
			new Sala("sala-01", "Sala 01", "Tamboré", 12),
			new Sala("sala-02", "Sala 02", "Tamboré", 10),
			new Sala("sala-03", "Sala 03", "Tamboré", 8));

	public List<Sala> buscar(LocalDate data, LocalTime inicio, LocalTime fim, Integer duracao, Integer pessoas) {
		validar(data, inicio, fim, duracao, pessoas);
		return SALAS.stream()
				.filter(sala -> sala.capacidade() >= pessoas)
				.toList();
	}

	private void validar(LocalDate data, LocalTime inicio, LocalTime fim, Integer duracao, Integer pessoas) {
		if (pessoas == null || pessoas < 1) {
			throw new ValidacaoBuscaException("A quantidade de pessoas deve ser ao menos 1.");
		}
		if (data.isBefore(LocalDate.now())) {
			throw new ValidacaoBuscaException("A data não pode estar no passado.");
		}
		if (fim == null && duracao == null) {
			throw new ValidacaoBuscaException("Informe o horário final ou a duração da reserva.");
		}
		if (duracao != null && duracao < 1) {
			throw new ValidacaoBuscaException("A duração deve ser de ao menos 1 minuto.");
		}
		LocalTime fimReserva = (fim != null) ? fim : inicio.plusMinutes(duracao);
		if (!fimReserva.isAfter(inicio)) {
			throw new ValidacaoBuscaException("O horário final deve ser posterior ao horário inicial.");
		}
	}
}
