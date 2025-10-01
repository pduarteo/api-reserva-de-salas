package com.pduarteo.reserva_salas.service;

import com.pduarteo.reserva_salas.model.Sala;
import com.pduarteo.reserva_salas.repository.SalaRepository;
import com.pduarteo.reserva_salas.support.exceptions.NomeAndarAlreadyExistsException;
import com.pduarteo.reserva_salas.support.exceptions.SalaNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SalaService {

    @Autowired
    private SalaRepository salaRepository;

    public Sala criarSala(Sala dadosSala){

        if(salaRepository.existsByNomeIgnoreCaseAndAndar(dadosSala.getNome(), dadosSala.getAndar())){
            throw new NomeAndarAlreadyExistsException(dadosSala.getNome(), dadosSala.getAndar());
        }

        Sala sala = new Sala();
        sala.setNome(dadosSala.getNome());
        sala.setCapacidade(dadosSala.getCapacidade());
        sala.setAndar(dadosSala.getAndar());
        sala.setRecursos(dadosSala.getRecursos());

        return salaRepository.save(sala);
    }

    public Sala buscarSalaPorId(Long id){
        return salaRepository.findById(id).orElseThrow(() -> new SalaNotFoundException(id));
    }

    public List<Sala> listarSalas() {
        return salaRepository.findAll();
    }

    public Sala atualizarSala(Long id, Sala sala){
        Sala salaExistente = buscarSalaPorId(id);
        salaExistente.setNome(sala.getNome());
        salaExistente.setCapacidade(sala.getCapacidade());
        salaExistente.setAndar(sala.getAndar());
        salaExistente.setRecursos(sala.getRecursos());
        return salaRepository.save(salaExistente);
    }

    public void deletarSala(Long id){
        Sala sala = buscarSalaPorId(id);
        salaRepository.delete(sala);
    }
}
