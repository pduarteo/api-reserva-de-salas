package com.pduarteo.reserva_salas.controller;

import com.pduarteo.reserva_salas.dto.CriarSalaDTO;
import com.pduarteo.reserva_salas.dto.RetornoSalaDTO;
import com.pduarteo.reserva_salas.model.Sala;
import com.pduarteo.reserva_salas.service.SalaService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping(path = "/api/salas")
public class SalaController {

    @Autowired
    private SalaService salaService;

    @GetMapping("/{id}")
    public ResponseEntity<RetornoSalaDTO> buscarSalaPorId(@PathVariable Long id){
        return ResponseEntity.ok().body(salaService.buscarSalaPorId(id));
    }

    @GetMapping
    public ResponseEntity<Page<RetornoSalaDTO>> buscarSalas(Pageable pageable){
        return ResponseEntity.ok().body(salaService.listarSalas(pageable));
    }

    @PostMapping
    public ResponseEntity<RetornoSalaDTO> criarSala(@RequestBody @Valid CriarSalaDTO dadosSala){
        RetornoSalaDTO novaSala = salaService.criarSala(dadosSala);
        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(novaSala.id())
                .toUri();
        return ResponseEntity.created(location).body(novaSala);
    }

    @PutMapping("/{id}")
    public ResponseEntity<RetornoSalaDTO> atualizarSala(@PathVariable Long id, @RequestBody @Valid CriarSalaDTO sala) {
        return ResponseEntity.ok().body(salaService.atualizarSala(id, sala));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletarSala(@PathVariable Long id) {
        salaService.deletarSala(id);
        return ResponseEntity.noContent().build();
    }
}
