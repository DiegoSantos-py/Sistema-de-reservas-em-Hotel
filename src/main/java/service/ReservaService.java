package service;

import model.Reserva;
import repository.ClienteRepository;
import repository.ReservaRepository;
import repository.QuartoRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class ReservaService {
    private final ClienteRepository repoCliente;
    private final ReservaRepository repoReserva;
    private final QuartoRepository repoQuarto;

    public ReservaService(ClienteRepository repoCliente, ReservaRepository repoReserva, QuartoRepository repoQuarto) {
        this.repoCliente = repoCliente;
        this.repoReserva = repoReserva;
        this.repoQuarto  = repoQuarto;
    }

    // -------------------------------------------------------------------------
    // Listagem
    // -------------------------------------------------------------------------

    public List<Reserva> listarTodas() {
        return new ArrayList<>(repoReserva.findAll().values());
    }

    public List<Reserva> listarPorDia(LocalDateTime dia) {
        if (dia == null) throw new IllegalArgumentException("O dia informado não pode ser nulo.");

        List<Reserva> resultado = new ArrayList<>();
        for (Reserva r : repoReserva.findAll().values()) {
            if (dia.equals(r.getDataHora())) resultado.add(r);
        }
        return resultado;
    }

    public List<Reserva> listarPorQuarto(Integer numQuarto) {
        if (numQuarto == null) throw new IllegalArgumentException("O número do quarto não pode ser nulo.");

        List<Reserva> resultado = new ArrayList<>();
        for (Reserva r : repoReserva.findAll().values()) {
            if (numQuarto.equals(r.getNumQuarto())) resultado.add(r);
        }
        return resultado;
    }

    public List<Reserva> listarPorCliente(Integer clienteId) {
        if (clienteId == null) throw new IllegalArgumentException("O id do cliente não pode ser nulo.");

        List<Reserva> resultado = new ArrayList<>();
        for (Reserva r : repoReserva.findAll().values()) {
            if (clienteId.equals(r.getClienteId())) resultado.add(r);
        }
        return resultado;
    }

    public Reserva buscarPorId(int id) {
        Reserva r = repoReserva.findById(id);
        if (r == null) throw new IllegalArgumentException("Não existe reserva com id " + id + ".");
        return r;
    }

    // -------------------------------------------------------------------------
    // Escrita
    // -------------------------------------------------------------------------

    public Reserva reservar(Reserva reserva) {
        if (reserva == null) throw new IllegalArgumentException("A reserva não pode ser nula.");

        validarIdParaCriacao(reserva.getId());
        validarCliente(reserva.getClienteId());
        validarQuarto(reserva.getNumQuarto());
        validarQuartoDisponivel(reserva.getNumQuarto(), null);

        repoReserva.save(reserva);
        return reserva;
    }

    public Reserva atualizar(Reserva reserva) {
        if (reserva == null) throw new IllegalArgumentException("A reserva não pode ser nula.");
        if (repoReserva.findById(reserva.getId()) == null)
            throw new IllegalArgumentException("Não existe reserva com id " + reserva.getId() + ".");

        validarCliente(reserva.getClienteId());
        validarQuarto(reserva.getNumQuarto());
        validarQuartoDisponivel(reserva.getNumQuarto(), reserva.getId());

        repoReserva.save(reserva);
        return reserva;
    }

    public boolean cancelar(int id) {
        if (repoReserva.findById(id) == null)
            throw new IllegalArgumentException("Não existe reserva com id " + id + ".");
        return repoReserva.deleteById(id);
    }

    // -------------------------------------------------------------------------
    // Validações
    // -------------------------------------------------------------------------

    private void validarIdParaCriacao(Integer id) {
        if (id == null) throw new IllegalArgumentException("O id da reserva não pode ser nulo.");
        if (repoReserva.findById(id) != null)
            throw new IllegalArgumentException("Já existe uma reserva com id " + id + ".");
    }

    private void validarCliente(Integer clienteId) {
        if (clienteId == null) throw new IllegalArgumentException("O id do cliente não pode ser nulo.");
        if (repoCliente.findById(clienteId) == null)
            throw new IllegalArgumentException("Cliente não encontrado para o id " + clienteId + ".");
    }

    private void validarQuarto(Integer numQuarto) {
        if (numQuarto == null) throw new IllegalArgumentException("O número do quarto não pode ser nulo.");
        if (repoQuarto.findById(numQuarto) == null)
            throw new IllegalArgumentException("Quarto não encontrado para o número " + numQuarto + ".");
    }

    private void validarQuartoDisponivel(Integer numQuarto, Integer idReservaAtual) {
        if (repoReserva.existsByRoom(numQuarto, idReservaAtual))
            throw new IllegalArgumentException("Já existe uma reserva para o quarto " + numQuarto + ".");
    }
}