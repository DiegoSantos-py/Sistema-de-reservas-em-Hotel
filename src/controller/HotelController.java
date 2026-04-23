package controller;

import exceptions.CapacidadeInvalidaException;
import exceptions.IdInvalidoException;
import exceptions.TipoInvalidoException;
import model.Quarto;
import model.Reserva;
import model.TIPO_QUARTO;
import service.ClienteService;
import service.QuartoService;
import service.ReservaService;
import view.ConsoleView;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
import java.time.LocalDateTime;

public class HotelController {
    private final ConsoleView view;
    private final QuartoService quarto;
    private final ClienteService clientes;
    private final ReservaService reservas;
    private boolean executando = true;

    public HotelController(ConsoleView view, QuartoService quarto,
                           ClienteService clientes, ReservaService reservas) {
        this.view = view;
        this.quarto = quarto;
        this.clientes = clientes;
        this.reservas = reservas;
    }

    public void loop() {
        while (executando) {
            view.showMenu();

            switch (view.lerOpcao()) {
                case 5 -> listarQuartos();
                case 6 -> cadastrarQuarto();
                case 7 -> atualizarQuarto();
                case 8 -> removerQuarto();

                case 9 -> listarReservas();
                case 10 -> listarReservasPorDia();
                case 11 -> listarReservasPorQuarto();
                case 12 -> cadastrarReserva();
                case 13 -> atualizarReserva();
                case 14 -> cancelarReserva();

                case 0 -> sair();
                default -> view.info("Opção inválida.");
            }
        }
    }

    private void listarQuartos() {
        List<Quarto> quartos = new ArrayList<>(quarto.listar().values());
        view.mostrarQuartos(quartos);
    }

    private void cadastrarQuarto() {
        try {
            int id = view.lerId("Id do quarto");
            int codigoTipo = view.lerOpcaoTipo("Tipo do quarto (1-SOLTEIRO, 2-CASAL, 3-LUXO, 4-SUITE)");
            int capacidade = view.lerId("Capacidade do quarto");

            Quarto q = quarto.criar(id, codigoTipo, capacidade);
            view.info("Quarto cadastrado com sucesso: " + q);

        } catch (IdInvalidoException | TipoInvalidoException | CapacidadeInvalidaException e) {
            view.erro(e.getMessage());
        } catch (RuntimeException e) {
            view.erro(e.getMessage());
        }
    }

    private void atualizarQuarto() {
        try {
            int id = view.lerId("Id do quarto");
            int codigoTipo = view.lerOpcaoTipo("Novo tipo do quarto (1-SOLTEIRO, 2-CASAL, 3-LUXO, 4-SUITE)");
            int capacidade = view.lerId("Nova capacidade do quarto");

            Quarto quartoAtualizado = quarto.atualizar(id, codigoTipo, capacidade);
            view.info("Quarto atualizado com sucesso: " + quartoAtualizado);

        } catch (IdInvalidoException | TipoInvalidoException | CapacidadeInvalidaException e) {
            view.erro(e.getMessage());
        } catch (RuntimeException e) {
            view.erro(e.getMessage());
        }
    }

    private void removerQuarto() {
        try {
            int id = view.lerId("Id do quarto");

            boolean removido = quarto.remover(id);

            if (removido) {
                view.info("Quarto removido com sucesso.");
            } else {
                view.info("Nenhum quarto foi removido.");
            }

        } catch (IdInvalidoException e) {
            view.erro(e.getMessage());
        } catch (RuntimeException e) {
            view.erro(e.getMessage());
        }
    }

    private void listarReservas() {
        try {
            List<Reserva> lista = reservas.listarTodas();
            view.mostrarReservas(lista, i -> (i + 1) + " - ");
        } catch (RuntimeException e) {
            view.erro(e.getMessage());
        }
    }

    private void listarReservasPorDia() {
        try {
            String dataTexto = view.lerTexto("Data da reserva (AAAA-MM-DD)");
            LocalDateTime data = LocalDateTime.parse(dataTexto);

            List<Reserva> lista = reservas.listarPorDia(data);
            view.mostrarReservas(lista, i -> (i + 1) + " - ");

        } catch (DateTimeParseException e) {
            view.erro("Data inválida. Use o formato AAAA-MM-DD.");
        } catch (RuntimeException e) {
            view.erro(e.getMessage());
        }
    }

    private void listarReservasPorQuarto() {
        try {
            int numQuarto = view.lerId("Número do quarto");
            List<Reserva> lista = reservas.listarPorQuarto(numQuarto);
            view.mostrarReservas(lista, i -> (i + 1) + " - ");

        } catch (RuntimeException e) {
            view.erro(e.getMessage());
        }
    }

    private void cadastrarReserva() {
        try {
            int id = view.lerId("Id da reserva");
            int clienteId = view.lerId("Id do cliente");
            int numQuarto = view.lerId("Número do quarto");
            String dataTexto = view.lerTexto("Data da reserva (AAAA-MM-DD)");

            LocalDateTime data = LocalDateTime.parse(dataTexto);

            Reserva reserva = new Reserva(id, clienteId, data, numQuarto);
            Reserva reservaCriada = reservas.reservar(reserva);

            view.info("Reserva cadastrada com sucesso: " + reservaCriada);

        } catch (DateTimeParseException e) {
            view.erro("Data inválida. Use o formato AAAA-MM-DD.");
        } catch (RuntimeException e) {
            view.erro(e.getMessage());
        }
    }

    private void atualizarReserva() {
        try {
            int id = view.lerId("Id da reserva");
            int clienteId = view.lerId("Novo id do cliente");
            int numQuarto = view.lerId("Novo número do quarto");
            String dataTexto = view.lerTexto("Nova data da reserva (AAAA-MM-DD)");

            LocalDateTime data = LocalDateTime.parse(dataTexto);

            Reserva reserva = new Reserva(id, clienteId, data, numQuarto);
            Reserva reservaAtualizada = reservas.atualizar(reserva);

            view.info("Reserva atualizada com sucesso: " + reservaAtualizada);

        } catch (DateTimeParseException e) {
            view.erro("Data inválida. Use o formato AAAA-MM-DD.");
        } catch (RuntimeException e) {
            view.erro(e.getMessage());
        }
    }

    private void cancelarReserva() {
        try {
            int id = view.lerId("Id da reserva");

            boolean removida = reservas.cancelar(id);

            if (removida) {
                view.info("Reserva cancelada com sucesso.");
            } else {
                view.info("Nenhuma reserva foi removida.");
            }

        } catch (RuntimeException e) {
            view.erro(e.getMessage());
        }
    }

    private void sair() {
        executando = false;
        view.info("Até logo!");
    }
}