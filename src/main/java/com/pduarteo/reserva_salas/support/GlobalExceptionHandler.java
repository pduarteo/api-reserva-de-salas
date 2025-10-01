package com.pduarteo.reserva_salas.support;

import com.pduarteo.reserva_salas.support.exceptions.NomeAndarAlreadyExistsException;
import com.pduarteo.reserva_salas.support.exceptions.SalaNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.net.URI;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(SalaNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ProblemDetail handleNotFoundException(SalaNotFoundException ex, HttpServletRequest request) {
        var pd = ProblemDetail.forStatusAndDetail(HttpStatus.NOT_FOUND, ex.getMessage());
        pd.setTitle("Sala Não Encontrada");
        pd.setType(URI.create(request.getRequestURI()));
        return pd;
    }

    @ExceptionHandler(NomeAndarAlreadyExistsException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ProblemDetail handleNomeAndarAlreadyExistsException(NomeAndarAlreadyExistsException ex, HttpServletRequest request) {
        var pd = ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, ex.getMessage());
        pd.setTitle("Conflito de Nome e Andar");
        pd.setType(URI.create(request.getRequestURI()));
        return pd;
    }
}
