package view;

import controller.ClienteController;
import controller.QuartoController;
import controller.ReservaController;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class MenuViewFx {
    private final Stage stage;
    private final ClienteController clienteController;
    private final ReservaController reservaController;
    private final QuartoController quartoController;

    public MenuViewFx(Stage stage,ClienteController clienteController, ReservaController reservaController, QuartoController quartoController) {
        this.stage = stage;
        this.clienteController = clienteController;
        this.reservaController = reservaController;
        this.quartoController = quartoController;
    }

    public void exibir() {
        Parent raiz = construirLayout();
        if (stage.getScene() == null) {
            stage.setScene(new Scene(raiz, 620, 430));
        } else {
            stage.getScene().setRoot(raiz);
        }
        stage.setTitle("Sistema de Hotel - Menu");
        stage.show();
    }

    private Parent construirLayout() {
        VBox raiz = new VBox(20);
        raiz.setPadding(new Insets(80));
        raiz.setAlignment(javafx.geometry.Pos.CENTER);

        Label titulo    = new Label("HOTEL");
        Label subtitulo = new Label("sistema de gerenciamento");
        titulo.getStyleClass().add("titulo");
        subtitulo.getStyleClass().add("subtitulo");

        HBox botoes  = criarBotoes();


        raiz.getChildren().addAll(titulo, subtitulo, botoes);
        return raiz;
    }

    private HBox criarBotoes() {
        Button botaoCliente = new Button("Cadastrar Cliente");
        Button botaoQuarto  = new Button("Cadastrar Quarto");
        Button botaoReserva = new Button("Fazer Reserva");

        botaoCliente.setOnAction(e ->
                stage.getScene().setRoot(new ClienteViewFx(stage, clienteController, this).construirLayout()));
        botaoQuarto.setOnAction(e ->
                stage.getScene().setRoot(new QuartoViewFx(stage, quartoController, this).construirLayout()));
        botaoReserva.setOnAction(e ->
                stage.getScene().setRoot(new ReservaViewFx(stage, reservaController, clienteController, this).construirLayout()));

        HBox b = new HBox(10, botaoCliente, botaoQuarto, botaoReserva);
        b.setAlignment(javafx.geometry.Pos.CENTER);
        return b;
    }
}