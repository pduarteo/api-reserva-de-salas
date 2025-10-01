package com.pduarteo.reserva_salas.dto;

import com.pduarteo.reserva_salas.enums.Recurso;
import jakarta.validation.constraints.*;

import java.util.Set;

public record CriarSalaDTO (

        @NotBlank
        @Size(min = 1, max = 50, message = "O nome deve ter entre 1 e 50 caracteres")
        String nome,

        @NotNull
        @Min(value = 1, message = "A capacidade deve ser entre 1 e 50")
        @Max(value = 50, message = "A capacidade deve ser entre 1 e 50")
        Integer capacidade,

        @NotNull
        @Min(value = 1, message = "O andar deve ser entre 1 e 20")
        @Max(value = 20, message = "O andar deve ser entre 1 e 20")
        Integer andar,

        Set<Recurso> recursos
) {
}
