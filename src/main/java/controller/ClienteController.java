package controller;

import exceptions.CapacidadeInvalidaException;
import exceptions.IdInvalidoException;
import exceptions.TipoInvalidoException;
import model.Cliente;
import model.Quarto;
import model.Reserva;
import service.ClienteService;
import service.QuartoService;
import service.ReservaService;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;

public class ClienteController {
    private final ClienteService clientes;

    public ClienteController(ClienteService clientes) {
        this.clientes = clientes;
    }
    public List<Cliente> listarClientes() {
        return clientes.listarTodos();
    }

    public void cadastrarCliente(int id, String nome, String cpf, String telefone) throws IdInvalidoException {
        clientes.criar(id, nome, cpf, telefone);
    }

    public void atualizarCliente(int id, String nome, String cpf, String telefone) throws IdInvalidoException {
        clientes.atualizar(id, nome, cpf, telefone);
    }

    public void deletarCliente(int id) throws IdInvalidoException {
        clientes.remover(id);
    }
}