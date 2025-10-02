package com.pduarteo.reserva_salas.repository;

import com.pduarteo.reserva_salas.model.Reserva;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalTime;

@Repository
public interface ReservaRepository extends JpaRepository<Reserva, Long> {

    boolean existsBySalaIdAndDataReservaAndHoraInicioLessThanAndHoraFimGreaterThan(
            Long salaId,
            LocalDate dataReserva,
            LocalTime horaFim,
            LocalTime horaInicio
    );
}
