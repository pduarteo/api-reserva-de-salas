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

/**
 * Controlador REST para operações relacionadas à entidade Sala.
 * Expõe endpoints para CRUD, utilizando DTOs para entrada e saída.
 * Destaca validações, uso de ResponseEntity e integração com paginação.
 */
@RestController
@RequestMapping(path = "/api/salas")
public class SalaController {

    @Autowired
    private SalaService salaService; // Injeta o serviço responsável pela lógica de negócio de Sala

    /**
     * Busca uma sala pelo ID.
     * Retorna 200 com o DTO da sala encontrada ou erro se não existir.
     */
    @GetMapping("/{id}")
    public ResponseEntity<RetornoSalaDTO> buscarSalaPorId(@PathVariable Long id){
        return ResponseEntity.ok().body(salaService.buscarSalaPorId(id));
    }

    /**
     * Lista todas as salas de forma paginada.
     * Utiliza o Pageable do Spring para paginação automática.
     */
    @GetMapping
    public ResponseEntity<Page<RetornoSalaDTO>> buscarSalas(Pageable pageable){
        return ResponseEntity.ok().body(salaService.listarSalas(pageable));
    }

    /**
     * Cria uma nova sala.
     * Valida os dados recebidos via DTO, retorna 201 com URI do recurso criado.
     * Destaca uso do ServletUriComponentsBuilder para construir a URI da nova sala.
     */
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

    /**
     * Atualiza uma sala existente.
     * Recebe o ID e os novos dados via DTO, retorna o DTO atualizado.
     */
    @PutMapping("/{id}")
    public ResponseEntity<RetornoSalaDTO> atualizarSala(@PathVariable Long id, @RequestBody @Valid CriarSalaDTO sala) {
        return ResponseEntity.ok().body(salaService.atualizarSala(id, sala));
    }

    /**
     * Remove uma sala pelo ID.
     * Retorna 204 (No Content) se a exclusão for bem-sucedida.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletarSala(@PathVariable Long id) {
        salaService.deletarSala(id);
        return ResponseEntity.noContent().build();
    }
}
