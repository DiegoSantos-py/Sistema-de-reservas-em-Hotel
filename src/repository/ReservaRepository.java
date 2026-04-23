package repository;
import model.Reserva;
import java.util.*;
import java.io.*;
import java.nio.file.Path;

public class ReservaRepository {
    private static final Path ARQUIVO = Path.of("Reserva.dat");
    private Map<Integer, Reserva> repo = new HashMap<>();

    public void save(Reserva r){
        updateRepoMap(r);
        writeRepo();
    }

    public Map<Integer, Reserva> updateRepoMap(Reserva r){
        this.repo = findAll();
        this.repo.put(r.getId(), r);
        return this.repo;
    }

    private void writeRepo(){
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(ARQUIVO.toFile()))){
            oos.writeObject(this.repo);
        } catch (IOException e) {
            throw new RuntimeException("Não foi possível gravar os dados no arquivo.", e);
        }
    }

    @SuppressWarnings("unchecked")
    public Map<Integer, Reserva> findAll(){
        try(ObjectInputStream ois = new ObjectInputStream(new FileInputStream(ARQUIVO.toFile()))){
            Map<Integer,Reserva> reservas = (Map<Integer, Reserva>) ois.readObject();
            return reservas;

        } catch (FileNotFoundException e) {
            // Caso normal: arquivo ainda não existe
            return new HashMap<>();

        } catch (EOFException e) {
            // Arquivo existe, mas está vazio
            return new HashMap<>();

        } catch (InvalidClassException e) {
            throw new RuntimeException(
                    "Os dados do arquivo são incompatíveis com a versão atual da classe Reserva.",
                    e
            );

        } catch (StreamCorruptedException e) {
            throw new RuntimeException(
                    "O arquivo de Reservas está corrompido ou não foi gravado por serialização válida.",
                    e
            );

        } catch (ClassNotFoundException e) {
            throw new RuntimeException(
                    "A classe necessária para reconstruir os objetos do arquivo não foi encontrada.",
                    e
            );

        } catch (IOException e) {
            throw new RuntimeException(
                    "Ocorreu um erro de entrada/saída ao ler o arquivo de Reservas.",
                    e
            );
        } 

    }

    public Reserva findById(int id){
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

    public boolean existsByRoom(Integer numQuarto, Integer id){
        this.repo = findAll();

        for (Reserva r : this.repo.values()) {
            boolean ehQuartoIgual = Objects.equals(numQuarto, r.getNumQuarto());
            boolean ehIdDiferente = (id == null || r.getId() != id);

            if (ehQuartoIgual && ehIdDiferente) {
                return true;
            }
        }
        return false;
    }

    public boolean existsByClient(Integer id){
        this.repo = findAll();

        for (Reserva r : this.repo.values()) {
            boolean ehIdIgual = Objects.equals(r.getClienteId(), id);

            if (ehIdIgual) {
                return true;
            }
        }
        return false;
    }


}
