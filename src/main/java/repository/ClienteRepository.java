package repository;

import java.io.*;
import model.Cliente;
import java.util.*;
import java.nio.file.Path;

public class ClienteRepository {
    private static final Path ARQUIVO = Path.of("Cliente.dat");

    // ObjectOutputStream que suprime o cabeçalho de stream no append
    private static class AppendingObjectOutputStream extends ObjectOutputStream {
        public AppendingObjectOutputStream(OutputStream out) throws IOException {
            super(out);
        }
        @Override
        protected void writeStreamHeader() throws IOException {
            // Suprime o cabeçalho — já existe no arquivo
            reset();
        }
    }

    private void appendCliente(Cliente c) {
        File arquivo = ARQUIVO.toFile();
        try {
            if (!arquivo.exists() || arquivo.length() == 0) {
                // Arquivo novo: ObjectOutputStream normal (escreve o cabeçalho)
                try (ObjectOutputStream oos = new ObjectOutputStream(
                        new FileOutputStream(arquivo, true))) {
                    oos.writeObject(c);
                }
            } else {
                // Arquivo existente: suprime o cabeçalho
                try (ObjectOutputStream oos = new AppendingObjectOutputStream(
                        new FileOutputStream(arquivo, true))) {
                    oos.writeObject(c);
                }
            }
        } catch (IOException e) {
            throw new RuntimeException("Não foi possível gravar o cliente no arquivo.", e);
        }
    }

    public void save(Cliente c) {
        // Se já existe, remove a entrada antiga antes de reescrever
        // (necessário pois não há update in-place em stream sequencial)
        reescreverSemId(c.getId());
        appendCliente(c);
    }

    // Lê todos, descarta o id informado, regrava o arquivo do zero
    private void reescreverSemId(int idParaRemover) {
        Map<Integer, Cliente> todos = findAll();
        todos.remove(idParaRemover);
        reescreverTodos(todos);
    }

    private void reescreverTodos(Map<Integer, Cliente> mapa) {
        File arquivo = ARQUIVO.toFile();
        arquivo.delete(); // remove para recriar sem cabeçalho duplicado
        for (Cliente c : mapa.values()) {
            appendCliente(c);
        }
    }

    public Map<Integer, Cliente> findAll() {
        File arquivo = ARQUIVO.toFile();
        Map<Integer, Cliente> resultado = new HashMap<>();

        if (!arquivo.exists() || arquivo.length() == 0) return resultado;

        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(arquivo))) {
            while (true) {
                try {
                    Cliente c = (Cliente) ois.readObject();
                    // Em caso de duplicatas, o último gravado vence
                    resultado.put(c.getId(), c);
                } catch (EOFException e) {
                    break; // fim do stream — normal
                }
            }
        } catch (InvalidClassException e) {
            throw new RuntimeException(
                    "Dados incompatíveis com a versão atual da classe Cliente.", e);
        } catch (StreamCorruptedException e) {
            throw new RuntimeException(
                    "Arquivo corrompido ou não gerado por serialização válida.", e);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(
                    "Classe necessária para reconstruir os objetos não encontrada.", e);
        } catch (IOException e) {
            throw new RuntimeException(
                    "Erro de I/O ao ler o arquivo de clientes.", e);
        }

        return resultado;
    }

    public Cliente findById(int id) {
        return findAll().get(id);
    }

    public boolean deleteById(int id) {
        Map<Integer, Cliente> todos = findAll();
        if (todos.remove(id) == null) return false;
            reescreverTodos(todos);
        return true;
    }

    public boolean existsByCpf(String cpf, Integer id) {
        for (Cliente c : findAll().values()) {
            boolean ehCpfIgual = cpf != null && cpf.equals(c.getCpf());
            boolean ehIdDiferente = (id == null || c.getId() != id);
            if (ehCpfIgual && ehIdDiferente) return true;
        }
        return false;
    }

    public boolean existsByTelefone(String telefone, Integer id) {
        for (Cliente c : findAll().values()) {
            boolean ehTelefoneIgual = telefone != null && telefone.equals(c.getTelefone());
            boolean ehIdDiferente = (id == null || c.getId() != id);
            if (ehTelefoneIgual && ehIdDiferente) return true;
        }
        return false;
    }
}