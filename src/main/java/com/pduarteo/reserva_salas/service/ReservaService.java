package com.pduarteo.reserva_salas.service;

import com.pduarteo.reserva_salas.dto.CriarReservaDTO;
import com.pduarteo.reserva_salas.dto.RetornoReservaDTO;
import com.pduarteo.reserva_salas.model.Reserva;
import com.pduarteo.reserva_salas.model.Sala;
import com.pduarteo.reserva_salas.repository.ReservaRepository;
import com.pduarteo.reserva_salas.repository.SalaRepository;
import com.pduarteo.reserva_salas.support.exceptions.RegraNegocioException;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.DayOfWeek;

/**
 * Serviço responsável pela lógica de negócio das reservas de salas.
 * Realiza validações essenciais antes de criar ou cancelar reservas, garantindo regras de negócio e integridade dos dados.
 */
@Service
public class ReservaService {

    @Autowired
    private ReservaRepository reservaRepository;
    @Autowired
    private SalaRepository salaRepository;

    /**
     * Cria uma nova reserva de sala.
     * Valida se a sala existe e está ativa, se os dados de data/hora estão corretos,
     * se não há conflito de horários e se a quantidade de pessoas não excede a capacidade.
     * @param dto DTO com os dados da reserva
     * @return RetornoReservaDTO com os dados da reserva criada
     * @throws RegraNegocioException em caso de violação de regras de negócio
     */
    @Transactional
    public RetornoReservaDTO criar(CriarReservaDTO dto) {
        // Busca a sala e valida se está ativa
        Sala sala = salaRepository.findById(dto.salaId())
                .filter(Sala::getAtiva)
                .orElseThrow(() -> new RegraNegocioException("Sala não encontrada ou inativa"));

        // Valida regras de data e hora da reserva
        validarDataHora(dto);

        // Valida se a quantidade de pessoas não excede a capacidade da sala
        if (dto.quantidadePessoas() != null && dto.quantidadePessoas() > sala.getCapacidade()) {
            throw new RegraNegocioException("Quantidade de pessoas excede a capacidade da sala");
        }

        // Verifica se existe conflito de horário com outra reserva
        boolean conflito = reservaRepository
                .existsBySalaIdAndDataReservaAndHoraInicioLessThanAndHoraFimGreaterThan(
                        dto.salaId(),
                        dto.dataReserva(),
                        dto.horaFim(),
                        dto.horaInicio()
                );

        if (conflito) {
            throw new RegraNegocioException("Conflito de horário: já existe reserva nesse intervalo");
        }

        // Cria e salva a reserva
        Reserva reserva = new Reserva();
        reserva.setSalaId(dto.salaId());
        reserva.setDataReserva(dto.dataReserva());
        reserva.setHoraInicio(dto.horaInicio());
        reserva.setHoraFim(dto.horaFim());
        reserva.setResponsavel(dto.responsavel());
        reserva.setEmailResponsavel(dto.emailResponsavel());
        reserva.setDescricao(dto.descricao());
        reserva.setQuantidadePessoas(dto.quantidadePessoas());

        Reserva saved = reservaRepository.save(reserva);

        // Retorna o DTO da reserva criada
        return new RetornoReservaDTO(
                saved.getId(),
                saved.getSalaId(),
                saved.getDataReserva(),
                saved.getHoraInicio(),
                saved.getHoraFim(),
                saved.getResponsavel(),
                saved.getEmailResponsavel(),
                saved.getDescricao(),
                saved.getQuantidadePessoas()
        );
    }

    // Cancelar somente se futura
    /**
     * Cancela uma reserva existente, desde que seja futura.
     * Valida se a reserva existe e se o horário de início ainda não passou.
     * @param id identificador da reserva
     * @return RetornoReservaDTO com os dados da reserva cancelada
     * @throws RegraNegocioException se a reserva não existir ou já tiver iniciado
     */
    @Transactional
    public RetornoReservaDTO cancelar(Long id) {
        Reserva reserva = reservaRepository.findById(id)
                .orElseThrow(() -> new RegraNegocioException("Reserva não encontrada"));

        // Verifica se a reserva é futura
        LocalDateTime inicio = LocalDateTime.of(reserva.getDataReserva(), reserva.getHoraInicio());
        if (inicio.isBefore(LocalDateTime.now(ZoneId.of("America/Fortaleza")))) {
            throw new RegraNegocioException("Reservas passadas não podem ser canceladas");
        }

        RetornoReservaDTO retorno = new RetornoReservaDTO(
                reserva.getId(),
                reserva.getSalaId(),
                reserva.getDataReserva(),
                reserva.getHoraInicio(),
                reserva.getHoraFim(),
                reserva.getResponsavel(),
                reserva.getEmailResponsavel(),
                reserva.getDescricao(),
                reserva.getQuantidadePessoas()
        );
        reservaRepository.delete(reserva);
        return retorno;
    }

    /**
     * Valida as regras de negócio para data e hora da reserva.
     * Garante que a reserva não seja feita para o passado, respeite horários e dias permitidos,
     * e que a duração e os intervalos estejam corretos.
     * @param dto DTO com os dados da reserva
     * @throws RegraNegocioException em caso de violação das regras
     */
    private void validarDataHora(CriarReservaDTO dto) {
        LocalDate data = dto.dataReserva();
        LocalTime inicio = dto.horaInicio();
        LocalTime fim = dto.horaFim();

        LocalDateTime agora = LocalDateTime.now(ZoneId.of("America/Fortaleza"));

        // não pode passado
        if (data.isBefore(LocalDate.now()) ||
                (data.isEqual(LocalDate.now()) && fim.isBefore(agora.toLocalTime()))) {
            throw new RegraNegocioException("Não é permitido reservar em datas passadas");
        }

        // fim > inicio
        if (!fim.isAfter(inicio)) {
            throw new RegraNegocioException("Hora fim deve ser maior que hora início");
        }

        // max 90 dias
        if (data.isAfter(LocalDate.now().plusDays(90))) {
            throw new RegraNegocioException("Não é permitido reservar com mais de 90 dias de antecedência");
        }

        // segunda a sexta
        DayOfWeek dow = data.getDayOfWeek();
        if (dow == DayOfWeek.SATURDAY || dow == DayOfWeek.SUNDAY) {
            throw new RegraNegocioException("Reservas só podem ser feitas de segunda a sexta");
        }

        // dentro de 08:00–18:00
        if (inicio.isBefore(LocalTime.of(8, 0)) || fim.isAfter(LocalTime.of(18, 0))) {
            throw new RegraNegocioException("Horário permitido apenas entre 08:00 e 18:00");
        }

        // duração mínima/máxima
        Duration dur = Duration.between(inicio, fim);
        if (dur.toMinutes() < 30) {
            throw new RegraNegocioException("Duração mínima é de 30 minutos");
        }
        if (dur.toHours() > 4) {
            throw new RegraNegocioException("Duração máxima é de 4 horas");
        }

        // intervalos de 30 em 30
        if (inicio.getMinute() % 30 != 0 || fim.getMinute() % 30 != 0) {
            throw new RegraNegocioException("Reservas só podem começar e terminar em intervalos de 30 minutos");
        }
    }
}
