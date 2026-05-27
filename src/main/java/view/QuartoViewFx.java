package view;

import controller.HotelController;
import exceptions.CapacidadeInvalidaException;
import exceptions.IdInvalidoException;
import exceptions.TipoInvalidoException;
import model.Quarto;
import model.TIPO_QUARTO;
import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.*;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class QuartoViewFx {
    private final Stage stage;
    private final HotelController controller;
    private final MenuViewFx menu;

    private TextField campoId;
    private TextField campoTipo;
    private TIPO_QUARTO tipoSelecionado;
    private TextField campoCapacidade;

    private TableView<Quarto> tabelaQuartos;

    public QuartoViewFx(Stage stage, HotelController controller, MenuViewFx menu) {
        this.stage      = stage;
        this.controller = controller;
        this.menu       = menu;
    }

    public Parent construirLayout() {
        VBox raiz = new VBox(20);
        raiz.setPadding(new Insets(50));

        Label titulo    = new Label("Cadastrar Quarto");
        GridPane formulario = criarFormulario();
        HBox botoes     = criarBotoes();
        tabelaQuartos   = criarTabela();

        raiz.getChildren().addAll(titulo, formulario, botoes, tabelaQuartos);
        return raiz;
    }

    // ── Formulário com os campos ──────────────────────────────────────
    private GridPane criarFormulario() {
        GridPane grid = new GridPane();
        grid.setHgap(12);
        grid.setVgap(14);

        campoId = new TextField();
        campoId.setPromptText("Ex: 101");

        campoTipo = new TextField();
        campoTipo.setPromptText("Selecione o tipo...");
        campoTipo.setEditable(false);

        Button btnEscolherTipo = new Button("▾ Escolher");
        btnEscolherTipo.setOnAction(e -> abrirPopupTipo());

        campoCapacidade = new TextField();
        campoCapacidade.setPromptText("Ex: 2");

        grid.add(new Label("ID:"),         0, 0);
        grid.add(campoId,                  1, 0);

        HBox linhaHBox = new HBox(8, campoTipo, btnEscolherTipo);
        grid.add(new Label("Tipo:"),       0, 1);
        grid.add(linhaHBox,                1, 1);

        grid.add(new Label("Capacidade:"), 0, 2);
        grid.add(campoCapacidade,          1, 2);

        return grid;
    }

    // ── Tabela de quartos cadastrados ─────────────────────────────────
    private TableView<Quarto> criarTabela() {
        TableView<Quarto> tabela = new TableView<>();
        tabela.setPlaceholder(new Label("Nenhum quarto cadastrado."));

        TableColumn<Quarto, Integer> colId = new TableColumn<>("ID");
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));

        TableColumn<Quarto, TIPO_QUARTO> colTipo = new TableColumn<>("Tipo");
        colTipo.setCellValueFactory(new PropertyValueFactory<>("tipo"));

        TableColumn<Quarto, Integer> colCapacidade = new TableColumn<>("Capacidade");
        colCapacidade.setCellValueFactory(new PropertyValueFactory<>("capacidade"));

        TableColumn<Quarto, Boolean> colStatus = new TableColumn<>("Disponível");
        colStatus.setCellValueFactory(new PropertyValueFactory<>("status"));

        tabela.getColumns().addAll(colId, colTipo, colCapacidade, colStatus);
        atualizarTabela(tabela);
        return tabela;
    }

    private void atualizarTabela(TableView<Quarto> tabela) {
        tabela.setItems(FXCollections.observableArrayList(
                controller.listarQuartos().values()
        ));
    }

    // ── Popup de seleção de tipo ──────────────────────────────────────
    private void abrirPopupTipo() {
        Stage popup = new Stage();
        popup.initModality(Modality.APPLICATION_MODAL);
        popup.initOwner(stage);
        popup.initStyle(StageStyle.UNDECORATED);

        VBox layout = new VBox(0);

        Label cabecalho = new Label("Escolha o tipo de quarto");
        cabecalho.setPadding(new Insets(14, 20, 10, 20));
        layout.getChildren().addAll(cabecalho, new Separator());

        for (TIPO_QUARTO tipo : TIPO_QUARTO.values()) {
            layout.getChildren().add(criarBotaoOpcao(tipo, popup));
        }

        Button cancelar = new Button("Cancelar");
        cancelar.setMaxWidth(Double.MAX_VALUE);
        cancelar.setOnAction(e -> popup.close());
        layout.getChildren().addAll(new Separator(), cancelar);

        popup.setScene(new javafx.scene.Scene(layout, 260, 225));
        popup.setX(stage.getX() + (stage.getWidth()  - 260) / 2);
        popup.setY(stage.getY() + (stage.getHeight() - 225) / 2);
        popup.showAndWait();
    }

    private Button criarBotaoOpcao(TIPO_QUARTO tipo, Stage popup) {
        Button btn = new Button(tipo.name());
        btn.setMaxWidth(Double.MAX_VALUE);
        btn.setAlignment(javafx.geometry.Pos.CENTER_LEFT);
        btn.setOnAction(e -> {
            tipoSelecionado = tipo;
            campoTipo.setText(tipo.name());
            popup.close();
        });
        return btn;
    }

    // ── Botões de ação ────────────────────────────────────────────────
    private HBox criarBotoes() {
        Button botaoSalvar = new Button("Salvar");
        botaoSalvar.setOnAction(e -> salvar());

        Button botaoVoltar = new Button("Voltar ao menu");
        botaoVoltar.setOnAction(e -> menu.exibir());

        return new HBox(10, botaoSalvar, botaoVoltar);
    }

    private void salvar() {
        int id         = Integer.parseInt(campoId.getText());
        int capacidade = Integer.parseInt(campoCapacidade.getText());

        try {
            controller.cadastrarQuarto(id, tipoSelecionado.getCodigo(), capacidade);
            mostrarInformacao("Cadastro de Quartos", "Quarto cadastrado com sucesso");
            atualizarTabela(tabelaQuartos); // ← atualiza a tabela após salvar
        } catch (IdInvalidoException e) {
            mostrarErro("Id inválido");
        } catch (CapacidadeInvalidaException e) {
            mostrarErro("Capacidade inválida");
        } catch (TipoInvalidoException e) {
            mostrarErro("Tipo inválido");
        }
    }

    private void mostrarErro(String mensagem) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Erro");
        alert.setHeaderText(null);
        alert.setContentText(mensagem);
        alert.showAndWait();
    }

    private void mostrarInformacao(String titulo, String mensagem) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensagem);
        alert.showAndWait();
    }
}