

import controller.HotelController;
import javafx.application.Application;
import javafx.stage.Stage;
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

        ClienteService clienteService = new ClienteService(clienteRepository);
        QuartoService quartoService   = new QuartoService(quartoRepository);
        ReservaService reservaService = new ReservaService(clienteRepository, reservaRepository, quartoRepository);

        HotelController controller = new HotelController(quartoService, clienteService, reservaService);

        MenuViewFx view = new MenuViewFx(stage, controller );
        view.exibir();
    }

    public static void main(String[] args) {
        launch(args);
    }
}