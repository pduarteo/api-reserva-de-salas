package com.pduarteo.reserva_salas.service;

import com.pduarteo.reserva_salas.dto.CriarSalaDTO;
import com.pduarteo.reserva_salas.dto.RetornoSalaDTO;
import com.pduarteo.reserva_salas.model.Sala;
import com.pduarteo.reserva_salas.repository.SalaRepository;
import com.pduarteo.reserva_salas.support.exceptions.RegraNegocioException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Serviço responsável pela lógica de negócio relacionada à entidade Sala.
 * Realiza operações de CRUD e validações específicas antes de persistir ou alterar dados.
 */
@Service
public class SalaService {

    @Autowired
    private SalaRepository salaRepository; // Repositório para acesso ao banco de dados de salas

    /**
     * Cria uma nova sala após validar se já existe uma sala com o mesmo nome e andar.
     * @param dadosSala DTO com os dados da sala a ser criada
     * @return RetornoSalaDTO com os dados da sala criada
     * @throws RegraNegocioException se já existir sala com mesmo nome e andar
     */
    public RetornoSalaDTO criarSala(CriarSalaDTO dadosSala){
        if(salaRepository.existsByNomeIgnoreCaseAndAndar(dadosSala.nome(), dadosSala.andar())){
            throw new RegraNegocioException("Já existe uma sala com esse nome nesse andar.");
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

    /**
     * Busca uma sala pelo ID.
     * @param id identificador da sala
     * @return RetornoSalaDTO com os dados da sala encontrada
     * @throws RegraNegocioException se a sala não for encontrada
     */
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
                .orElseThrow(() -> new RegraNegocioException("Sala com ID " + id + " não encontrada."));
    }

    /**
     * Lista todas as salas de forma paginada.
     * @param pageable informações de paginação
     * @return página de RetornoSalaDTO
     */
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

    /**
     * Atualiza os dados de uma sala existente.
     * @param id identificador da sala
     * @param sala DTO com os novos dados
     * @return RetornoSalaDTO com os dados atualizados
     * @throws RegraNegocioException se a sala não for encontrada
     */
    public RetornoSalaDTO atualizarSala(Long id, CriarSalaDTO sala){
        Sala salaExistente = salaRepository.findById(id)
                .orElseThrow(() -> new RegraNegocioException("Sala com ID " + id + " não encontrada."));
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

    /**
     * Remove uma sala do sistema e retorna os dados da sala removida.
     * @param id identificador da sala
     * @return RetornoSalaDTO com os dados da sala removida
     * @throws RegraNegocioException se a sala não for encontrada
     */
    public RetornoSalaDTO deletarSala(Long id){
        Sala sala = salaRepository.findById(id)
                .orElseThrow(() -> new RegraNegocioException("Sala com ID " + id + " não encontrada."));
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
