package com.pduarteo.reserva_salas.service;

import com.pduarteo.reserva_salas.dto.CriarSalaDTO;
import com.pduarteo.reserva_salas.dto.RetornoSalaDTO;
import com.pduarteo.reserva_salas.model.Sala;
import com.pduarteo.reserva_salas.repository.SalaRepository;
import com.pduarteo.reserva_salas.support.exceptions.NomeAndarAlreadyExistsException;
import com.pduarteo.reserva_salas.support.exceptions.SalaNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SalaService {

    @Autowired
    private SalaRepository salaRepository;

    public RetornoSalaDTO criarSala(CriarSalaDTO dadosSala){

        if(salaRepository.existsByNomeIgnoreCaseAndAndar(dadosSala.nome(), dadosSala.andar())){
            throw new NomeAndarAlreadyExistsException(dadosSala.nome(), dadosSala.andar());
        }

        Sala sala = new Sala();
        sala.setNome(dadosSala.nome());
        sala.setCapacidade(dadosSala.capacidade());
        sala.setAndar(dadosSala.andar());
        sala.setRecursos(dadosSala.recursos());

        salaRepository.save(sala);
        return new RetornoSalaDTO(
                sala.getId(),
                sala.getNome(),
                sala.getCapacidade(),
                sala.getAndar(),
                sala.getRecursos(),
                sala.getAtiva()
        );
    }

    public RetornoSalaDTO buscarSalaPorId(Long id){
        return salaRepository.findById(id)
                .map(sala -> new RetornoSalaDTO(
                        sala.getId(),
                        sala.getNome(),
                        sala.getCapacidade(),
                        sala.getAndar(),
                        sala.getRecursos(),
                        sala.getAtiva()
                ))
                .orElseThrow(() -> new SalaNotFoundException(id));
    }

    public Page<RetornoSalaDTO> listarSalas(Pageable pageable) {
        Page<Sala> salas = salaRepository.findAll(pageable);
        return salas.map(sala -> new RetornoSalaDTO(
                sala.getId(),
                sala.getNome(),
                sala.getCapacidade(),
                sala.getAndar(),
                sala.getRecursos(),
                sala.getAtiva()
        ));
    }

    public RetornoSalaDTO atualizarSala(Long id, CriarSalaDTO sala){
        Sala salaExistente = salaRepository.findById(id)
                .orElseThrow(() -> new SalaNotFoundException(id));
        salaExistente.setNome(sala.nome());
        salaExistente.setCapacidade(sala.capacidade());
        salaExistente.setAndar(sala.andar());
        salaExistente.setRecursos(sala.recursos());
        salaRepository.save(salaExistente);
        return new RetornoSalaDTO(
                salaExistente.getId(),
                salaExistente.getNome(),
                salaExistente.getCapacidade(),
                salaExistente.getAndar(),
                salaExistente.getRecursos(),
                salaExistente.getAtiva()
        );
    }

    public RetornoSalaDTO deletarSala(Long id){
        Sala sala = salaRepository.findById(id)
                .orElseThrow(() -> new SalaNotFoundException(id));
        RetornoSalaDTO retorno = new RetornoSalaDTO(
                sala.getId(),
                sala.getNome(),
                sala.getCapacidade(),
                sala.getAndar(),
                sala.getRecursos(),
                sala.getAtiva()
        );
        salaRepository.delete(sala);
        return retorno;
    }
}
