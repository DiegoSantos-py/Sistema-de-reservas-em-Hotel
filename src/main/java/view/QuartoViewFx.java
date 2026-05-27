package view;

import controller.HotelController;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class QuartoViewFx {
    private final Stage stage;
    private final HotelController controller;
    private final MenuViewFx menu;

    public QuartoViewFx(Stage stage, HotelController controller, MenuViewFx menu) {
        this.stage      = stage;
        this.controller = controller;
        this.menu       = menu;
    }

    public Parent construirLayout() {
        VBox raiz = new VBox(20);
        raiz.setPadding(new Insets(50));

        Label titulo     = new Label("Cadastrar Quarto");
        HBox botoes      = criarBotoes();

        raiz.getChildren().addAll(titulo, botoes);
        return raiz;
    }

    private HBox criarBotoes() {
        Button botaoVoltar = new Button("Voltar ao menu");
        botaoVoltar.setOnAction(e -> menu.exibir());
        return new HBox(10, botaoVoltar);
    }
}