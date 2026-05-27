package view;

import controller.HotelController;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class MenuViewFx {
    private final Stage stage;
    private final HotelController controller;

    public MenuViewFx(Stage stage, HotelController controller) {
        this.stage      = stage;
        this.controller = controller;
    }

    public void exibir() {
        Parent raiz = construirLayout();

        if (stage.getScene() == null) {
            Scene scene = new Scene(raiz, 620, 480);
            scene.getStylesheets().add(
                    getClass().getResource("/menu.css").toExternalForm()
            );
            stage.setScene(scene);
        } else {
            stage.getScene().setRoot(raiz);
        }

        stage.setTitle("Sistema de Hotel");
        stage.show();
    }

    public Parent construirLayout() {
        VBox raiz = new VBox(12);
        raiz.getStyleClass().add("container");
        raiz.setPadding(new Insets(80));
        raiz.setAlignment(Pos.CENTER);

        Label titulo    = new Label("HOTEL");
        Label subtitulo = new Label("sistema de gerenciamento");
        titulo.getStyleClass().add("titulo");
        subtitulo.getStyleClass().add("subtitulo");

        VBox botoes = criarBotoes();

        raiz.getChildren().addAll(titulo, subtitulo, botoes);
        return raiz;
    }

    private VBox criarBotoes() {
        Button botaoCliente = new Button("👤  Clientes");
        Button botaoQuarto  = new Button("🛏  Quartos");
        Button botaoReserva = new Button("📋  Reservas");

        for (Button b : new Button[]{botaoCliente, botaoQuarto, botaoReserva}) {
            b.getStyleClass().add("botao-menu");
            b.setMaxWidth(Double.MAX_VALUE);
        }

        botaoCliente.setOnAction(e ->
                stage.getScene().setRoot(new ClienteViewFx(stage, controller, this).construirLayout()));
        botaoQuarto.setOnAction(e ->
                stage.getScene().setRoot(new QuartoViewFx(stage, controller, this).construirLayout()));
        botaoReserva.setOnAction(e ->
                stage.getScene().setRoot(new ReservaViewFx(stage, controller, this).construirLayout()));

        VBox vbox = new VBox(10, botaoCliente, botaoQuarto, botaoReserva);
        vbox.setAlignment(Pos.CENTER);
        return vbox;
    }
}