package view;

import controller.ClienteController;
import exceptions.IdInvalidoException;
import model.Cliente;
import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.*;
import javafx.stage.Stage;



public class ClienteViewFx {
    private final Stage stage;
    private final ClienteController clienteController;
    private final MenuViewFx menu;



    private TextField campoId;
    private TextField campoNome;
    private TextField campoTelefone;
    private TextField campoCPF;
    private TextField campoDeleteId;
    private TableView<Cliente> tabelaClientes;

    public ClienteViewFx(Stage stage, ClienteController clienteController, MenuViewFx menu) {
        this.stage      = stage;
        this.clienteController = clienteController;
        this.menu       = menu;
    }

    public Parent construirLayout() {
        VBox raiz = new VBox(20);
        raiz.setPadding(new Insets(50));

        Label titulo    = new Label("Cadastrar Quarto");
        GridPane formulario = criarFormulario();
        HBox botoes     = criarBotoes();
        tabelaClientes   = criarTabela();
        HBox formDeletarCliente = criarFormularioDeletar();
        raiz.getChildren().addAll(titulo, formulario, botoes, tabelaClientes, formDeletarCliente);
        return raiz;
    }

    // ── Formulário com os campos ──────────────────────────────────────
    private GridPane criarFormulario() {
        GridPane grid = new GridPane();
        grid.setHgap(12);
        grid.setVgap(14);

        campoId = new TextField();
        campoId.setPromptText("Ex: 101");

        campoNome = new TextField();
        campoNome.setPromptText("Ex: João da Silva");

        campoTelefone = new TextField();
        campoTelefone.setPromptText("Ex: 75912345678");

        campoCPF = new TextField();
        campoCPF.setPromptText("Ex: 00000000000");

        grid.add(new Label("ID:"), 0, 0);
        grid.add(campoId, 1, 0);

        grid.add(new Label("Nome:"), 0, 1);
        grid.add(campoNome, 1, 1);

        grid.add(new Label("Telefone:"), 0, 2);
        grid.add(campoTelefone, 1, 2);

        grid.add(new Label("CPF:"), 0, 3);
        grid.add(campoCPF, 1, 3);

        return grid;
    }

    private HBox criarFormularioDeletar() {
        campoDeleteId = new TextField();
        campoDeleteId.setPromptText("Id do cliente");

        Button botaoDeletar = new Button("Deletar");

        botaoDeletar.setOnAction(e -> deletarCliente());

        return new HBox(10,
                new Label("Deletar cliente por Id:"),
                campoDeleteId,
                botaoDeletar
        );
    }

    // ── Tabela de quartos cadastrados ─────────────────────────────────
    private TableView<Cliente> criarTabela() {
        TableView<Cliente> tabela = new TableView<>();
        tabela.setPlaceholder(new Label("Nenhum cliente cadastrado."));

        TableColumn<Cliente, Integer> colId = new TableColumn<>("ID");
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));

        TableColumn<Cliente, String> colNome = new TableColumn<>("Nome");
        colNome.setCellValueFactory(new PropertyValueFactory<>("nome"));

        TableColumn<Cliente, String> colTelefone = new TableColumn<>("Telefone");
        colTelefone.setCellValueFactory(new PropertyValueFactory<>("telefone"));

        TableColumn<Cliente, String> colCPF = new TableColumn<>("CPF");
        colCPF.setCellValueFactory(new PropertyValueFactory<>("cpf"));

        tabela.getColumns().addAll(colId, colNome, colTelefone, colCPF);
        atualizarTabela(tabela);
        return tabela;
    }

    private void atualizarTabela(TableView<Cliente> tabela) {
        tabela.setItems(FXCollections.observableArrayList(
                clienteController.listarClientes()
        ));
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
        int id = Integer.parseInt(campoId.getText());
        String nome =  campoNome.getText();
        String telefone =  campoTelefone.getText();
        String CPF =  campoCPF.getText();

        try {
            clienteController.cadastrarCliente(id, nome, CPF, telefone);
            mostrarInformacao("Cadastro de Clientes", "Cliente cadastrado com sucesso");
            atualizarTabela(tabelaClientes); // ← atualiza a tabela após salvar
        } catch (IdInvalidoException e) {
            mostrarErro("Id inválido" + e);
        } catch (IllegalArgumentException e) {
            mostrarErro("Dado Inválido," + e);
        } catch (IllegalStateException e) {
            mostrarErro("Dado já utilizado," + e);
        }
    }

    private void deletarCliente(){
        int id =Integer.parseInt( campoDeleteId.getText());
        try {
            clienteController.deletarCliente(id);
            atualizarTabela(tabelaClientes);
        } catch (IdInvalidoException e) {
            throw new RuntimeException(e);
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