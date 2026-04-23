package service;

import model.Reserva;
import repository.ClienteRepository;
import repository.ReservaRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ReservaService {
    private ClienteRepository repoCliente;
    private ReservaRepository repoReserva;

    public ReservaService(ClienteRepository repoCliente, ReservaRepository repoReserva) {
        this.repoCliente = repoCliente;
        this.repoReserva = repoReserva;
    }

    public List<Reserva> listarPorDia(LocalDateTime dia) {
        if (dia == null) {
            throw new IllegalArgumentException("O dia informado não pode ser nulo.");
        }

        Map<Integer, Reserva> reservas = repoReserva.findAll();
        List<Reserva> reservasDoDia = new ArrayList<>();

        for (Reserva r : reservas.values()) {
            if (dia.equals(r.getDataHora())) {
                reservasDoDia.add(r);
            }
        }

        return reservasDoDia;
    }

    public List<Reserva> listarPorQuarto(Integer numQuarto) {
        if (numQuarto == null) {
            throw new IllegalArgumentException("O número do quarto não pode ser nulo.");
        }

        Map<Integer, Reserva> reservas = repoReserva.findAll();
        List<Reserva> reservasDoQuarto = new ArrayList<>();

        for (Reserva r : reservas.values()) {
            if (numQuarto.equals(r.getNumQuarto())) {
                reservasDoQuarto.add(r);
            }
        }

        return reservasDoQuarto;
    }

    public List<Reserva> listarTodas() {
        return new ArrayList<>(repoReserva.findAll().values());
    }

    public Reserva reservar(Reserva reserva) {
        if (reserva == null) {
            throw new IllegalArgumentException("A reserva não pode ser nula.");
        }

        validarIdParaCriacao(reserva.getId());
        validarCliente(reserva.getClienteId());
        validarQuartoDisponivel(reserva.getNumQuarto(), null);

        repoReserva.save(reserva);
        return reserva;
    }

    public boolean cancelar(int id) {
        return repoReserva.deleteById(id);
    }

    public Reserva atualizar(Reserva reserva) {
        if (reserva == null) {
            throw new IllegalArgumentException("A reserva não pode ser nula.");
        }

        Reserva existente = repoReserva.findById(reserva.getId());

        if (existente == null) {
            throw new IllegalArgumentException("Não existe reserva com id " + reserva.getId() + ".");
        }

        validarCliente(reserva.getClienteId());
        validarQuartoDisponivel(reserva.getNumQuarto(), reserva.getId());

        repoReserva.save(reserva);
        return reserva;
    }

    private void validarIdParaCriacao(Integer id) {
        if (id == null) {
            throw new IllegalArgumentException("O id da reserva não pode ser nulo.");
        }

        if (repoReserva.findById(id) != null) {
            throw new IllegalArgumentException("Já existe uma reserva com id " + id + ".");
        }
    }

    private void validarCliente(Integer clienteId) {
        if (clienteId == null) {
            throw new IllegalArgumentException("O id do cliente não pode ser nulo.");
        }

        if (repoCliente.findById(clienteId) == null) {
            throw new IllegalArgumentException("Cliente não encontrado para o id " + clienteId + ".");
        }
    }

    private void validarQuartoDisponivel(Integer numQuarto, Integer idReservaAtual) {
        if (numQuarto == null) {
            throw new IllegalArgumentException("O número do quarto não pode ser nulo.");
        }

        if (repoReserva.existsByRoom(numQuarto, idReservaAtual)) {
            throw new IllegalArgumentException(
                "Já existe uma reserva para o quarto " + numQuarto + "."
            );
        }
    }
}