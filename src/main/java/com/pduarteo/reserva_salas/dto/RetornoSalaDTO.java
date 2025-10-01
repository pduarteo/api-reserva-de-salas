package com.pduarteo.reserva_salas.dto;

import com.pduarteo.reserva_salas.enums.Recurso;

import java.util.Set;

public record RetornoSalaDTO (
        Long id,
        String nome,
        Integer capacidade,
        Integer andar,
        Set<Recurso> recursos,
        Boolean ativa
) {
}
