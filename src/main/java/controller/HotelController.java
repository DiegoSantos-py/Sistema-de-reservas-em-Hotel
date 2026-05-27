package controller;

import exceptions.CapacidadeInvalidaException;
import exceptions.IdInvalidoException;
import exceptions.TipoInvalidoException;
import model.Cliente;
import model.Quarto;
import model.Reserva;
import service.ClienteService;
import service.QuartoService;
import service.ReservaService;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;

public class HotelController {
    private final QuartoService quarto;
    private final ClienteService clientes;
    private final ReservaService reservas;

    private static final DateTimeFormatter FORMATTER =
            DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    public HotelController(QuartoService quarto,
                           ClienteService clientes,
                           ReservaService reservas) {
        this.quarto   = quarto;
        this.clientes = clientes;
        this.reservas = reservas;
    }

    // -------------------------------------------------------------------------
    // Clientes
    // -------------------------------------------------------------------------

    public List<Cliente> listarClientes() {
        return clientes.listarTodos();
    }

    public void cadastrarCliente(int id, String nome, String cpf, String telefone) {
        clientes.criar(id, nome, cpf, telefone);
    }

    public void atualizarCliente(int id, String nome, String cpf, String telefone) {
        clientes.atualizar(id, nome, cpf, telefone);
    }

    public void removerCliente(int id) {
        if (!reservas.listarPorCliente(id).isEmpty()) {
            throw new IllegalStateException("Cliente possui reservas ativas e não pode ser removido.");
        }
        clientes.remover(id);
    }

    // -------------------------------------------------------------------------
    // Quartos
    // -------------------------------------------------------------------------

    public Map<Integer, Quarto> listarQuartos() {
        return quarto.listar();
    }

    public void cadastrarQuarto(int id, int codigoTipo, int capacidade)
            throws IdInvalidoException, TipoInvalidoException, CapacidadeInvalidaException {
        quarto.criar(id, codigoTipo, capacidade);
    }

    public void atualizarQuarto(int id, int codigoTipo, int capacidade)
            throws IdInvalidoException, TipoInvalidoException, CapacidadeInvalidaException {
        quarto.atualizar(id, codigoTipo, capacidade);
    }

    public void removerQuarto(int id) throws IdInvalidoException {
        if (!reservas.listarPorQuarto(id).isEmpty()) {
            throw new IllegalStateException("Quarto possui reservas ativas e não pode ser removido.");
        }
        quarto.remover(id);
    }

    // -------------------------------------------------------------------------
    // Reservas
    // -------------------------------------------------------------------------

    public List<Reserva> listarReservas() {
        return reservas.listarTodas();
    }

    public List<Reserva> listarReservasPorDia(String dataTexto) {
        LocalDateTime data = LocalDateTime.parse(dataTexto, FORMATTER);
        return reservas.listarPorDia(data);
    }

    public List<Reserva> listarReservasPorQuarto(int numQuarto) {
        return reservas.listarPorQuarto(numQuarto);
    }

    public List<Reserva> listarReservasPorCliente(int clienteId) {
        return reservas.listarPorCliente(clienteId);
    }

    public void cadastrarReserva(int id, int clienteId, int numQuarto, String dataTexto) {
        LocalDateTime data = LocalDateTime.parse(dataTexto, FORMATTER);
        reservas.reservar(new Reserva(id, clienteId, data, numQuarto));

    }

    public void atualizarReserva(int id, int clienteId, int numQuarto, String dataTexto) {
        LocalDateTime data = LocalDateTime.parse(dataTexto, FORMATTER);
        reservas.atualizar(new Reserva(id, clienteId, data, numQuarto));
    }

    public void cancelarReserva(int id) {
        reservas.cancelar(id);
    }
}