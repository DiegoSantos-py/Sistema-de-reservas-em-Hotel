package repository;

import java.io.*;
import model.Quarto;
import java.util.*;
import java.nio.file.Path;

public class QuartoRepository {
    private static final Path ARQUIVO = Path.of("Quarto.bin");
    private Map<Integer, Quarto> repo = new HashMap<>();

    private void writeRepo() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(ARQUIVO.toFile()))) {
            oos.writeObject(this.repo);
        } catch (IOException e) {
            throw new RuntimeException("Não foi possível gravar os dados no arquivo.", e);
        }
    }

    public void save(Quarto q){
        updateRepoMap(q);
        writeRepo();
    }

    private Map<Integer, Quarto> updateRepoMap(Quarto q){
        this.repo = findAll();
        repo.put(q.getId(), q);
        return repo;
    }

    @SuppressWarnings("unchecked")
    public Map<Integer, Quarto> findAll(){
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(ARQUIVO.toFile()))){
            Map<Integer, Quarto> quartos = (Map<Integer, Quarto>) ois.readObject();
            return quartos;

        } catch (FileNotFoundException e) {
            return new HashMap<>();

        } catch (EOFException e) {
            return new HashMap<>();

        } catch (InvalidClassException e) {
            throw new RuntimeException(
                    "Os dados do arquivo são incompatíveis com a versão atual da classe Quarto.",
                    e
            );

        } catch (StreamCorruptedException e) {
            throw new RuntimeException(
                    "O arquivo de quartos está corrompido ou não foi gravado por serialização válida.",
                    e
            );

        } catch (ClassNotFoundException e) {
            throw new RuntimeException(
                    "A classe necessária para reconstruir os objetos do arquivo não foi encontrada.",
                    e
            );

        } catch (IOException e) {
            throw new RuntimeException(
                    "Ocorreu um erro de entrada/saída ao ler o arquivo de quartos.",
                    e
            );
        } 
    }

    public Quarto findById(int id){
        this.repo = findAll();
        return this.repo.get(id);
    }

    public boolean deleteById(int id){
        this.repo = findAll();

        if (this.repo.remove(id) == null) {
            return false;
        }

        writeRepo();
        return true;
    }
}