package service;

import model.Cliente;
import repository.ClienteRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ClienteService {

    private final ClienteRepository repo;

    public ClienteService(ClienteRepository repo) {
        this.repo = repo;
    }

    // -------------------------------------------------------------------------
    // Listagem e busca
    // -------------------------------------------------------------------------

    public List<Cliente> listarTodos() {
        return new ArrayList<>(repo.findAll().values());
    }

    public Cliente buscarPorId(int id) {
        Cliente c = repo.findById(id);
        if (c == null) {
            throw new IllegalArgumentException("Cliente com id " + id + " não encontrado.");
        }
        return c;
    }

    // -------------------------------------------------------------------------
    // Criação
    // -------------------------------------------------------------------------

    public Cliente criar(int id, String nome, String cpf, String telefone) {
        Cliente c = new Cliente(id, nome, cpf, telefone);
        validar(c, null);
        repo.save(c);
        return c;
    }

    public Cliente atualizar(int id, String nome, String cpf, String telefone) {
        if (repo.findById(id) == null)
            throw new IllegalArgumentException("Cliente com id " + id + " não encontrado.");

        Cliente c = new Cliente(id, nome, cpf, telefone);
        validar(c, id);
        repo.save(c);
        return c;
    }


    // -------------------------------------------------------------------------
    // Remoção
    // -------------------------------------------------------------------------

    public void remover(int id) {
        if (repo.findById(id) == null) {
            throw new IllegalArgumentException("Cliente com id " + id + " não encontrado.");
        }
        boolean removido = repo.deleteById(id);
        if (!removido) {
            throw new RuntimeException("Falha ao remover o cliente com id " + id + ".");
        }
    }

    // -------------------------------------------------------------------------
    // Validação
    // -------------------------------------------------------------------------

    /**
     * Valida os campos do cliente e verifica unicidade de CPF e telefone.
     *
     * @param c  o cliente a validar
     * @param id id a ignorar nas checagens de unicidade (null para novos registros)
     */
    private void validar(Cliente c, Integer id) {
        if (c == null) {
            throw new IllegalArgumentException("Cliente não pode ser nulo.");
        }
        if (c.getNome() == null || c.getNome().isBlank()) {
            throw new IllegalArgumentException("Nome do cliente é obrigatório.");
        }
        if (c.getCpf() == null || c.getCpf().isBlank()) {
            throw new IllegalArgumentException("CPF do cliente é obrigatório.");
        }
        if (!cpfFormatoValido(c.getCpf())) {
            throw new IllegalArgumentException("CPF inválido: " + c.getCpf());
        }
        if (c.getTelefone() == null || c.getTelefone().isBlank()) {
            throw new IllegalArgumentException("Telefone do cliente é obrigatório.");
        }
        if (repo.existsByCpf(c.getCpf(), id)) {
            throw new IllegalStateException("Já existe um cliente cadastrado com o CPF " + c.getCpf() + ".");
        }
        if (repo.existsByTelefone(c.getTelefone(), id)) {
            throw new IllegalStateException("Já existe um cliente cadastrado com o telefone " + c.getTelefone() + ".");
        }
    }

    // -------------------------------------------------------------------------
    // Helpers privados
    // -------------------------------------------------------------------------

    /** Aceita apenas dígitos, com ou sem formatação (000.000.000-00 ou 00000000000). */
    private boolean cpfFormatoValido(String cpf) {
        String digitos = cpf.replaceAll("[.\\-]", "");
        return digitos.matches("\\d{11}");
    }
}