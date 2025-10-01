package com.pduarteo.reserva_salas.support.exceptions;

public class NomeAndarAlreadyExistsException extends RuntimeException {
    public NomeAndarAlreadyExistsException(String nome, Integer andar) {
        super("Já existe uma sala com o nome: " + nome + "no andar: " + andar);
    }
}
