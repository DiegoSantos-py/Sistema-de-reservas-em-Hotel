package repository;
import java.io.*;
import model.Cliente;
import java.util.*;
import java.nio.file.Path;

public class ClienteRepository {
    private static final Path ARQUIVO = Path.of("Cliente.bin");
    private Map<Integer, Cliente> repo = new HashMap<>();

    private void writeRepo() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(ARQUIVO.toFile()))) {
            oos.writeObject(this.repo);
        } catch (IOException e) {
            throw new RuntimeException("Não foi possível gravar os dados no arquivo.", e);
        }
    }

    public void save(Cliente c){
        updateRepoMap(c);
        writeRepo();
    }

    private Map<Integer, Cliente> updateRepoMap(Cliente c){
        this.repo = findAll();
        repo.put(c.getId(), c);
        return repo;
    }

    @SuppressWarnings("unchecked")
    public Map<Integer, Cliente> findAll(){
        
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(ARQUIVO.toFile()))){
            Map<Integer, Cliente> clientes = (Map<Integer, Cliente>) ois.readObject();
            return clientes;

        } catch (FileNotFoundException e) {
            // Caso normal: arquivo ainda não existe
            return new HashMap<>();

        } catch (EOFException e) {
            // Arquivo existe, mas está vazio
            return new HashMap<>();

        } catch (InvalidClassException e) {
            throw new RuntimeException(
                    "Os dados do arquivo são incompatíveis com a versão atual da classe Cliente.",
                    e
            );

        } catch (StreamCorruptedException e) {
            throw new RuntimeException(
                    "O arquivo de clientes está corrompido ou não foi gravado por serialização válida.",
                    e
            );

        } catch (ClassNotFoundException e) {
            throw new RuntimeException(
                    "A classe necessária para reconstruir os objetos do arquivo não foi encontrada.",
                    e
            );

        } catch (IOException e) {
            throw new RuntimeException(
                    "Ocorreu um erro de entrada/saída ao ler o arquivo de clientes.",
                    e
            );
        } 
        
    }

    public Cliente findById(int id){
        this.repo = findAll();
        return this.repo.get(id);
    }

    public boolean deleteById(int id){
        this.repo = findAll();
        if (this.repo.remove(id) == null) return false;
        
        // grava o mapa atualizado no arquivo
        writeRepo();

        return true;
    }

    public boolean existsByCpf(String cpf, Integer id){
        this.repo = findAll();

        for (Cliente c : this.repo.values()) {

            boolean ehCpfIgual = cpf != null && cpf.equals(c.getCpf());
            boolean ehIdDiferente = (id == null || c.getId() != id);

            if (ehCpfIgual && ehIdDiferente) {
                return true;
            }
        }
        return false;
    }

    public boolean existsByTelefone(String telefone, Integer id){
        this.repo = findAll();

        for (Cliente c : this.repo.values()) {

            boolean ehTelefoneIgual = telefone != null && telefone.equals(c.getTelefone());
            boolean ehIdDiferente = (id == null || c.getId() != id);

            if (ehTelefoneIgual && ehIdDiferente) {
                return true;
            }
        }
        return false;
    }
        
}

