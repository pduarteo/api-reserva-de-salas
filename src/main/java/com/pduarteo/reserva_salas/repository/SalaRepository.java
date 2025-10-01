package com.pduarteo.reserva_salas.repository;

import com.pduarteo.reserva_salas.model.Sala;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SalaRepository extends JpaRepository<Sala, Long> {
    boolean existsByNomeIgnoreCaseAndAndar(String nome, Integer andar);
}
