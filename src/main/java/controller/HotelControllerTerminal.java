package controller;

import exceptions.CapacidadeInvalidaException;
import exceptions.IdInvalidoException;
import exceptions.TipoInvalidoException;
import model.Reserva;
import service.ClienteService;
import service.QuartoService;
import service.ReservaService;
import view.ConsoleView;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;

public class HotelControllerTerminal {
    private final ConsoleView view;
    private final QuartoService quarto;
    private final ClienteService clientes;
    private final ReservaService reservas;
    private boolean executando = true;

    // Formato explícito para evitar ambiguidade no parse
    private static final DateTimeFormatter FORMATTER =
            DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    public HotelControllerTerminal(ConsoleView view, QuartoService quarto,
                                   ClienteService clientes, ReservaService reservas) {
        this.view     = view;
        this.quarto   = quarto;
        this.clientes = clientes;
        this.reservas = reservas;
    }

    public void loop() {
        while (executando) {
            view.showMenu();

            switch (view.lerOpcao()) {
                case 1  -> listarClientes();
                case 2  -> cadastrarCliente();
                case 3  -> atualizarCliente();
                case 4  -> removerCliente();

                case 5  -> listarQuartos();
                case 6  -> cadastrarQuarto();
                case 7  -> atualizarQuarto();
                case 8  -> removerQuarto();

                case 9  -> listarReservas();
                case 10 -> listarReservasPorDia();
                case 11 -> listarReservasPorQuarto();
                case 12 -> listarReservasPorCliente();
                case 13 -> cadastrarReserva();
                case 14 -> atualizarReserva();
                case 15 -> cancelarReserva();

                case 0  -> sair();
                default -> view.info("Opção inválida.");
            }
        }
    }

    // -------------------------------------------------------------------------
    // Clientes
    // -------------------------------------------------------------------------

    private void listarClientes() {
        try {
            view.mostrarClientes(clientes.listarTodos());
        } catch (RuntimeException e) {
            view.erro(e.getMessage());
        }
    }

    private void cadastrarCliente() {
        try {
            // adapte os métodos de leitura conforme os campos de Cliente
            int id = view.lerId("");
            String nome     = view.lerTexto("Nome do cliente");
            String cpf      = view.lerTexto("CPF do cliente");
            String telefone = view.lerTexto("Telefone do cliente");

            view.info("Cliente cadastrado: " + clientes.criar(id, nome, cpf, telefone));
        } catch (RuntimeException e) {
            view.erro(e.getMessage());
        }
    }

    private void atualizarCliente() {
        try {
            int    id       = view.lerId("Id do cliente");
            String nome     = view.lerTexto("Novo nome");
            String cpf      = view.lerTexto("Novo CPF");
            String telefone = view.lerTexto("Novo telefone");

            view.info("Cliente atualizado: " + clientes.atualizar(id, nome, cpf, telefone));
        } catch (RuntimeException e) {
            view.erro(e.getMessage());
        }
    }

    private void removerCliente() {
        try {
            int id = view.lerId("Id do cliente");

            // impede remoção se houver reservas vinculadas
            if (reservas.listarPorCliente(id).isEmpty()) {
                clientes.remover(id);
                view.info("Cliente removido com sucesso.");
            } else {
                view.erro("Não é possível remover: cliente possui reservas ativas.");
            }
        } catch (RuntimeException e) {
            view.erro(e.getMessage());
        }
    }

    // -------------------------------------------------------------------------
    // Quartos
    // -------------------------------------------------------------------------

    private void listarQuartos() {
        try {
            view.mostrarQuartos(new ArrayList<>(quarto.listar().values()));
        } catch (RuntimeException e) {
            view.erro(e.getMessage());
        }
    }

    private void cadastrarQuarto() {
        try {
            int id          = view.lerId("Id do quarto");
            int codigoTipo  = view.lerOpcaoTipo("Tipo (1-SOLTEIRO, 2-CASAL, 3-LUXO, 4-SUITE)");
            int capacidade  = view.lerId("Capacidade");

            view.info("Quarto cadastrado: " + quarto.criar(id, codigoTipo, capacidade));
        } catch (IdInvalidoException | TipoInvalidoException | CapacidadeInvalidaException e) {
            view.erro(e.getMessage());
        } catch (RuntimeException e) {
            view.erro(e.getMessage());
        }
    }

    private void atualizarQuarto() {
        try {
            int id         = view.lerId("Id do quarto");
            int codigoTipo = view.lerOpcaoTipo("Novo tipo (1-SOLTEIRO, 2-CASAL, 3-LUXO, 4-SUITE)");
            int capacidade = view.lerId("Nova capacidade");

            view.info("Quarto atualizado: " + quarto.atualizar(id, codigoTipo, capacidade));
        } catch (IdInvalidoException | TipoInvalidoException | CapacidadeInvalidaException e) {
            view.erro(e.getMessage());
        } catch (RuntimeException e) {
            view.erro(e.getMessage());
        }
    }

    private void removerQuarto() {
        try {
            int id = view.lerId("Id do quarto");

            // impede remoção se houver reservas vinculadas
            if (reservas.listarPorQuarto(id).isEmpty()) {
                quarto.remover(id);
                view.info("Quarto removido com sucesso.");
            } else {
                view.erro("Não é possível remover: quarto possui reservas ativas.");
            }
        } catch (IdInvalidoException e) {
            view.erro(e.getMessage());
        } catch (RuntimeException e) {
            view.erro(e.getMessage());
        }
    }

    // -------------------------------------------------------------------------
    // Reservas
    // -------------------------------------------------------------------------

    private void listarReservas() {
        try {
            view.mostrarReservas(reservas.listarTodas(), i -> (i + 1) + " - ");
        } catch (RuntimeException e) {
            view.erro(e.getMessage());
        }
    }

    private void listarReservasPorDia() {
        try {
            String dataTexto = view.lerTexto("Data e hora (AAAA-MM-DD HH:mm)");
            LocalDateTime data = LocalDateTime.parse(dataTexto, FORMATTER);
            view.mostrarReservas(reservas.listarPorDia(data), i -> (i + 1) + " - ");
        } catch (DateTimeParseException e) {
            view.erro("Data inválida. Use o formato AAAA-MM-DD HH:mm.");
        } catch (RuntimeException e) {
            view.erro(e.getMessage());
        }
    }

    private void listarReservasPorQuarto() {
        try {
            int numQuarto = view.lerId("Número do quarto");
            view.mostrarReservas(reservas.listarPorQuarto(numQuarto), i -> (i + 1) + " - ");
        } catch (RuntimeException e) {
            view.erro(e.getMessage());
        }
    }

    private void listarReservasPorCliente() {
        try {
            int clienteId = view.lerId("Id do cliente");
            view.mostrarReservas(reservas.listarPorCliente(clienteId), i -> (i + 1) + " - ");
        } catch (RuntimeException e) {
            view.erro(e.getMessage());
        }
    }

    private void cadastrarReserva() {
        try {
            int    id        = view.lerId("Id da reserva");
            int    clienteId = view.lerId("Id do cliente");
            int    numQuarto = view.lerId("Número do quarto");
            String dataTexto = view.lerTexto("Data e hora (AAAA-MM-DD HH:mm)");

            LocalDateTime data = LocalDateTime.parse(dataTexto, FORMATTER);
            view.info("Reserva criada: " + reservas.reservar(new Reserva(id, clienteId, data, numQuarto)));
        } catch (DateTimeParseException e) {
            view.erro("Data inválida. Use o formato AAAA-MM-DD HH:mm.");
        } catch (RuntimeException e) {
            view.erro(e.getMessage());
        }
    }

    private void atualizarReserva() {
        try {
            int    id        = view.lerId("Id da reserva");
            int    clienteId = view.lerId("Novo id do cliente");
            int    numQuarto = view.lerId("Novo número do quarto");
            String dataTexto = view.lerTexto("Nova data e hora (AAAA-MM-DD HH:mm)");

            LocalDateTime data = LocalDateTime.parse(dataTexto, FORMATTER);
            view.info("Reserva atualizada: " + reservas.atualizar(new Reserva(id, clienteId, data, numQuarto)));
        } catch (DateTimeParseException e) {
            view.erro("Data inválida. Use o formato AAAA-MM-DD HH:mm.");
        } catch (RuntimeException e) {
            view.erro(e.getMessage());
        }
    }

    private void cancelarReserva() {
        try {
            int id = view.lerId("Id da reserva");
            reservas.cancelar(id);
            view.info("Reserva cancelada com sucesso.");
        } catch (RuntimeException e) {
            view.erro(e.getMessage());
        }
    }

    private void sair() {
        executando = false;
        view.info("Até logo!");
    }
}