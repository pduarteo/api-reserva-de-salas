package com.pduarteo.reserva_salas.controller;

import com.pduarteo.reserva_salas.dto.CriarReservaDTO;
import com.pduarteo.reserva_salas.dto.RetornoReservaDTO;
import com.pduarteo.reserva_salas.service.ReservaService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/reservas")
public class ReservaController {

    @Autowired
    private ReservaService reservaService;

    @PostMapping
    public ResponseEntity<RetornoReservaDTO> criarReserva(@Valid @RequestBody CriarReservaDTO criarReservaDTO) {
        RetornoReservaDTO reserva = reservaService.criarReserva(criarReservaDTO);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(reserva.id())
                .toUri();
        return ResponseEntity.created(location).body(reserva);
    }

    @GetMapping
    public ResponseEntity<Page<RetornoReservaDTO>> listarReservas(@PageableDefault Pageable pageable) {
        return ResponseEntity.ok(reservaService.listarReservas(pageable));
    }

    @GetMapping("/{id}")
    public ResponseEntity<RetornoReservaDTO> buscarReservaPorId(@PathVariable Long id) {
        return ResponseEntity.ok(reservaService.buscarReservaPorId(id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> cancelarReserva(@PathVariable Long id) {
        reservaService.cancelarReserva(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/sala/{salaId}")
    public ResponseEntity<List<RetornoReservaDTO>> listarReservasPorSala(@PathVariable Long salaId) {
        return ResponseEntity.ok(reservaService.listarReservasPorSalaId(salaId));
    }

    @GetMapping("responsavel/{responsavel}")
    public ResponseEntity<List<RetornoReservaDTO>> listarReservasPorResponsavel(@RequestParam String responsavel) {
        return ResponseEntity.ok(reservaService.listarReservasPorEmailResponsavel(responsavel));
    }
}
