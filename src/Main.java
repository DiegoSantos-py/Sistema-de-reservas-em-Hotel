import controller.HotelController;
import model.Cliente;
import repository.ClienteRepository;
import repository.QuartoRepository;
import repository.ReservaRepository;
import service.ClienteService;
import service.QuartoService;
import service.ReservaService;
import view.ConsoleView;

public class Main{
    public static void main(String [] Args){
            var clienteRepo = new ClienteRepository();
            var reservaRepo = new ReservaRepository();
            var quartoRepo = new QuartoRepository();

            ClienteService clienteService = new ClienteService();
            ReservaService reservaService = new ReservaService();
            QuartoService quartoService = new QuartoService(quartoRepo);

            ConsoleView consoleView = new ConsoleView();

            HotelController controller = new HotelController(consoleView, quartoService, clienteService, reservaService);

            controller.loop();
        
    }
}