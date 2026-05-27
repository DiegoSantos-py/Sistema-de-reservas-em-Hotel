package view;

import model.Cliente;
import model.Quarto;
import model.Reserva;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Scanner;
import java.util.function.IntFunction;

public class ConsoleView {
    private final Scanner sc = new Scanner(System.in);
    private final DateTimeFormatter DTF = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
    private final DateTimeFormatter DF = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    public void showMenu() {
        System.out.println("\n=== Hotel (MVC/CLI) ===");
        System.out.println("1) Listar clientes");
        System.out.println("2) Cadastrar cliente");
        System.out.println("3) Atualizar cliente");
        System.out.println("4) Remover cliente");
        System.out.println("5) Listar quartos");
        System.out.println("6) Cadastrar quarto");
        System.out.println("7) Atualizar quarto");
        System.out.println("8) Remover quarto");
        System.out.println("9) Listar reservas");
        System.out.println("10) Cadastrar reserva");
        System.out.println("11) Atualizar reserva");
        System.out.println("12) Cancelar reserva");
        System.out.println("13) Listar reservas por dia");
        System.out.println("0) Sair");
        System.out.print("Escolha: ");
    }

    public int lerOpcao() {
        try {
            return Integer.parseInt(sc.nextLine().trim());
        } catch (Exception e) {
            return -1;
        }
    }

    public int lerOpcaoTipo(String rotulo) {
        System.out.print(rotulo + ": ");
        try {
            return Integer.parseInt(sc.nextLine().trim());
        } catch (Exception e) {
            return -1;
        }
    }

    public String lerTexto(String rotulo) {
        System.out.print(rotulo + ": ");
        return sc.nextLine();
    }

    public int lerId(String rotulo) {
        while (true) {
            System.out.print(rotulo + " (número): ");
            try {
                return Integer.parseInt(sc.nextLine().trim());
            } catch (Exception e) {
                System.out.println("Valor inválido. Tente novamente.");
            }
        }
    }

    public LocalDateTime lerDataHora(String rotulo) {
        while (true) {
            System.out.print(rotulo + " (dd/MM/yyyy HH:mm): ");
            try {
                return LocalDateTime.parse(sc.nextLine().trim(), DTF);
            } catch (Exception e) {
                System.out.println("Formato inválido. Ex.: 25/08/2025 14:30");
            }
        }
    }

    public LocalDate lerData(String rotulo) {
        while (true) {
            System.out.print(rotulo + " (dd/MM/yyyy): ");
            try {
                return LocalDate.parse(sc.nextLine().trim(), DF);
            } catch (Exception e) {
                System.out.println("Formato inválido. Ex.: 25/08/2025");
            }
        }
    }

    public void mostrarClientes(List<Cliente> clientes) {
        if (clientes.isEmpty()) {
            System.out.println("(sem clientes)");
        } else {
            clientes.forEach(c -> System.out.println(" - " + c));
        }
    }

    public void mostrarQuartos(List<Quarto> quartos) {
        if (quartos.isEmpty()) {
            System.out.println("(sem quartos)");
        } else {
            quartos.forEach(q -> System.out.println(" - " + q));
        }
    }

    public void mostrarReservas(List<Reserva> reservas, IntFunction<String> nomeClienteById) {
        if (reservas.isEmpty()) {
            System.out.println("(sem reservas)");
            return;
        }

        for (Reserva r : reservas) {
            String nomeCliente = nomeClienteById.apply(r.getClienteId());
            String dataHora = r.getDataHora().format(DTF);

            System.out.printf(" - #%d | %s | Cliente %d - %s | Quarto %d%n",
                    r.getId(),
                    dataHora,
                    r.getClienteId(),
                    nomeCliente,
                    r.getNumQuarto());
        }
    }

    public void info(String msg) {
        System.out.println(msg);
    }

    public void erro(String msg) {
        System.out.println("Erro: " + msg);
    }
}