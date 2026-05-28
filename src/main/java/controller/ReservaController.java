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

public class ReservaController {
    private final ReservaService reservas;

    private static final DateTimeFormatter FORMATTER =
            DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    public ReservaController(ReservaService reservas) {
        this.reservas = reservas;
    }
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

    public boolean cancelarReserva(int id) throws IdInvalidoException {
        return reservas.cancelar(id);
    }
}