package com.pduarteo.reserva_salas.dto;

import jakarta.validation.constraints.*;

import java.time.LocalDate;
import java.time.LocalTime;

public record CriarReservaDTO(
        @NotNull
        Long salaId,

        @NotNull
        LocalDate dataReserva,

        @NotNull
        LocalTime horaInicio,

        @NotNull
        LocalTime horaFim,

        @NotBlank @Size(min = 3, max = 100)
        String responsavel,

        @NotBlank @Email
        String emailResponsavel,

        @Size(max = 255)
        String descricao,

        @Min(1)
        Integer quantidadePessoas
) {
}
