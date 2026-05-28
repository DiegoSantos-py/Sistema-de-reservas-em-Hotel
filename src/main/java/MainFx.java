

import controller.ClienteController;
import controller.QuartoController;
import controller.ReservaController;
import javafx.application.Application;
import javafx.stage.Stage;
import model.Cliente;
import repository.ClienteRepository;
import repository.QuartoRepository;
import repository.ReservaRepository;
import service.ClienteService;
import service.QuartoService;
import service.ReservaService;
import view.MenuViewFx;
import view.ReservaViewFx;

public class MainFx extends Application {

    @Override
    public void start(Stage stage) {
        ClienteRepository clienteRepository = new ClienteRepository();
        QuartoRepository quartoRepository   = new QuartoRepository();
        ReservaRepository reservaRepository = new ReservaRepository();

        ClienteService clienteService = new ClienteService(clienteRepository, reservaRepository);
        QuartoService quartoService   = new QuartoService(quartoRepository, reservaRepository);
        ReservaService reservaService = new ReservaService(clienteRepository, reservaRepository, quartoRepository);

        ClienteController clienteController = new ClienteController(clienteService);
        QuartoController quartoController = new QuartoController(quartoService);
        ReservaController reservaController = new ReservaController(reservaService);

        MenuViewFx view = new MenuViewFx(stage, clienteController, reservaController, quartoController );
        view.exibir();
    }

    public static void main(String[] args) {
        launch(args);
    }
}