package com.pduarteo.reserva_salas.controller;

import com.pduarteo.reserva_salas.model.Sala;
import com.pduarteo.reserva_salas.service.SalaService;
import org.springframework.beans.factory.annotation.Autowired;
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
    public ResponseEntity<Sala> buscarSalaPorId(@PathVariable Long id){
        return ResponseEntity.ok().body(salaService.buscarSalaPorId(id));
    }

    @GetMapping
    public ResponseEntity<List<Sala>> buscarSalas(){
        return ResponseEntity.ok().body(salaService.listarSalas());
    }

    @PostMapping
    public ResponseEntity<Sala> criarSala(@RequestBody Sala dadosSala){
        Sala novaSala = salaService.criarSala(dadosSala);
        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(novaSala.getId())
                .toUri();
        return ResponseEntity.created(location).body(novaSala);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Sala> atualizarSala(@PathVariable Long id, @RequestBody Sala sala) {
        return ResponseEntity.ok().body(salaService.atualizarSala(id, sala));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletarSala(@PathVariable Long id) {
        salaService.deletarSala(id);
        return ResponseEntity.noContent().build();
    }
}
