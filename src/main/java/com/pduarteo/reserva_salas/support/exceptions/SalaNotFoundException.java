package com.pduarteo.reserva_salas.support.exceptions;

public class SalaNotFoundException extends RuntimeException {
    public SalaNotFoundException(Long id) {
        super("Sala com ID: " + id + " não encontrada.");
    }
}
