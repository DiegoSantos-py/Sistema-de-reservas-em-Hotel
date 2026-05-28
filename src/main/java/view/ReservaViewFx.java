package view;

import controller.ClienteController;
import controller.ReservaController;
import exceptions.IdInvalidoException;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import model.Reserva;

import java.time.format.DateTimeParseException;

public class ReservaViewFx {
    private final Stage stage;
    private final ReservaController reservaController;
    private final ClienteController clienteController;
    private final MenuViewFx menu;

    private TextField campoId;
    private TextField campoDeleteId;
    private TextField campoClienteId;
    private TextField campoNumQuarto;
    private TextField campoDataHora;

    private final ObservableList<String> itensDaLista = FXCollections.observableArrayList();
    private ListView<String> listaReservas;

    public ReservaViewFx(Stage stage, ReservaController reservaController, ClienteController clienteController, MenuViewFx menu) {
        this.stage      = stage;
        this.reservaController = reservaController;
        this.clienteController = clienteController;
        this.menu       = menu;
    }

    public Parent construirLayout() {
        VBox raiz = new VBox(15);
        raiz.setPadding(new Insets(20));

        Label titulo       = new Label("Fazer Reserva");
        GridPane formulario = criarFormulario();
        HBox botoes        = criarBotoes();
        HBox formularioDeletar = criarFormularioDeletar();

        listaReservas = new ListView<>(itensDaLista);
        listaReservas.setPrefHeight(180);


        atualizarLista();
        raiz.getChildren().addAll(titulo, formulario, botoes,
                new Label("Reservas cadastradas:"), listaReservas, formularioDeletar);

        return raiz;
    }

    private void cadastrarReserva() {
        try {
            int id        = Integer.parseInt(campoId.getText());
            int clienteId = Integer.parseInt(campoClienteId.getText());
            int numQuarto = Integer.parseInt(campoNumQuarto.getText());
            String dataHora = campoDataHora.getText();

            reservaController.cadastrarReserva(id, clienteId, numQuarto, dataHora);
            mostrarInformacao("Reserva cadastrada", "Reserva cadastrada com sucesso.");
            limparCampos();
            atualizarLista();

        } catch (NumberFormatException e) {
            mostrarErro("Id, quarto e cliente devem ser números inteiros.");
        } catch (DateTimeParseException e) {
            mostrarErro("Data inválida. Use o formato: yyyy-MM-dd HH:mm");
        } catch (IllegalArgumentException | IllegalStateException e) {
            mostrarErro(e.getMessage());
        } catch (RuntimeException e) {
            mostrarErro("Erro inesperado: " + e.getMessage());
        }
    }

    private void deletarReserva() {
        try {
            int id = Integer.parseInt(campoDeleteId.getText());

            boolean removed = reservaController.cancelarReserva(id);

            if (removed) {
                mostrarInformacao("Sucesso", "Reserva removida.");
                atualizarLista();
            } else {
                mostrarErro("Reserva não encontrada.");
            }

            campoDeleteId.clear();

        } catch (NumberFormatException e) {
            mostrarErro("Id deve ser um número inteiro.");
        } catch (IllegalStateException | IllegalArgumentException | IdInvalidoException e) {
            mostrarErro(e.getMessage());
        }
    }

    private void atualizarLista() {
        itensDaLista.clear();
        for (Reserva r : reservaController.listarReservas()) {
            itensDaLista.add(r.toString());
        }
        if (reservaController.listarReservas().isEmpty()) {
            itensDaLista.add("Nenhuma reserva cadastrada.");
        }
    }

    private GridPane criarFormulario() {
        campoId        = new TextField(); campoId.setPromptText("Ex: 1");
        campoNumQuarto = new TextField(); campoNumQuarto.setPromptText("Ex: 2");
        campoClienteId = new TextField(); campoClienteId.setPromptText("Ex: 4");
        campoDataHora  = new TextField(); campoDataHora.setPromptText("2025-01-31 14:30");

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.add(new Label("Id:"),        0, 0); grid.add(campoId,        1, 0);
        grid.add(new Label("Quarto:"),    0, 1); grid.add(campoNumQuarto, 1, 1);
        grid.add(new Label("Cliente:"),   0, 2); grid.add(campoClienteId, 1, 2);
        grid.add(new Label("Data/Hora:"), 0, 3); grid.add(campoDataHora,  1, 3);
        return grid;
    }

    private HBox criarFormularioDeletar() {
        campoDeleteId = new TextField();
        campoDeleteId.setPromptText("Id da reserva");

        Button botaoDeletar = new Button("Deletar");

        botaoDeletar.setOnAction(e -> deletarReserva());

        HBox box = new HBox(10,
                new Label("Deletar reserva por Id:"),
                campoDeleteId,
                botaoDeletar
        );

        return box;
    }


    private HBox criarBotoes() {
        Button botaoAdicionar = new Button("Adicionar");
        Button botaoListar    = new Button("Atualizar lista");
        Button botaoLimpar    = new Button("Limpar campos");
        Button botaoVoltar    = new Button("Voltar ao menu");

        botaoAdicionar.setOnAction(e -> cadastrarReserva());
        botaoListar.setOnAction(e -> atualizarLista());
        botaoLimpar.setOnAction(e -> limparCampos());
        botaoVoltar.setOnAction(e -> menu.exibir());

        return new HBox(10, botaoAdicionar, botaoListar, botaoLimpar, botaoVoltar);
    }

    private void limparCampos() {
        campoId.clear();
        campoClienteId.clear();
        campoNumQuarto.clear();
        campoDataHora.clear();
    }

    private void mostrarInformacao(String titulo, String mensagem) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensagem);
        alert.showAndWait();
    }

    private void mostrarErro(String mensagem) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Erro");
        alert.setHeaderText(null);
        alert.setContentText(mensagem);
        alert.showAndWait();
    }
}