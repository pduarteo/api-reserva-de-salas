package com.pduarteo.reserva_salas.dto;

import java.time.LocalDate;
import java.time.LocalTime;

public record RetornoReservaDTO(
        Long id,
        Long salaId,
        LocalDate dataReserva,
        LocalTime horaInicio,
        LocalTime horaFim,
        String responsavel,
        String emailResponsavel,
        String descricao,
        Integer quantidadePessoas
) {
}
