package controller;

import java.util.ArrayList;
import java.util.List;

import exceptions.CapacidadeInvalidaException;
import exceptions.IdInvalidoException;
import exceptions.TipoInvalidoException;
import model.Quarto;
import model.TIPO_QUARTO;
import service.ClienteService;
import service.QuartoService;
import service.ReservaService;
import view.ConsoleView;

public class HotelController {
    private final ConsoleView view;
    private final QuartoService quarto;
    private final ClienteService clientes;
    private final ReservaService reservas;

    public HotelController(ConsoleView view, QuartoService quartos, ClienteService clientes, ReservaService reservas){
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
            //case 0 -> sair();
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
            String tipoTexto = view.lerTexto("Tipo do quarto (solteiro, casal, luxo, suite)");
            int capacidade = view.lerId("Capacidade do quarto");

            TIPO_QUARTO tipo = TIPO_QUARTO.valueOf(tipoTexto.toLowerCase());

            Quarto quarto = quarto.criar(id, tipo, capacidade);
            view.info("Quarto cadastrado com sucesso: " + quarto);

        } catch (IllegalArgumentException e) {
            view.erro("Tipo de quarto inválido.");
        } catch (IdInvalidoException | TipoInvalidoException | CapacidadeInvalidaException e) {
            view.erro(e.getMessage());
        }
    }

    private void atualizarQuarto() {
        try {
            int id = view.lerId("Id do quarto");
            String tipoTexto = view.lerTexto("Novo tipo do quarto (solteiro, casal, luxo, suite)");
            int capacidade = view.lerId("Nova capacidade do quarto");

            TIPO_QUARTO tipo = TIPO_QUARTO.valueOf(tipoTexto.toLowerCase());

            Quarto quartoAtualizado = quarto.atualizar(id, tipo, capacidade);
            view.info("Quarto atualizado com sucesso: " + quartoAtualizado);

        } catch (IllegalArgumentException e) {
            view.erro("Tipo de quarto inválido.");
        } catch (IdInvalidoException | TipoInvalidoException | CapacidadeInvalidaException e) {
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
        }
    }

}
