package repository;

import java.io.*;
import model.Quarto;
import java.util.*;
import java.nio.file.Path;

public class QuartoRepository {
    private static final Path ARQUIVO = Path.of("Quarto.dat");

    // Suprime o cabeçalho de stream ao fazer append
    private static class AppendingObjectOutputStream extends ObjectOutputStream {
        public AppendingObjectOutputStream(OutputStream out) throws IOException {
            super(out);
        }
        @Override
        protected void writeStreamHeader() throws IOException {
            reset();
        }
    }

    private void appendQuarto(Quarto q) {
        File arquivo = ARQUIVO.toFile();
        try {
            if (!arquivo.exists() || arquivo.length() == 0) {
                try (ObjectOutputStream oos = new ObjectOutputStream(
                        new FileOutputStream(arquivo, true))) {
                    oos.writeObject(q);
                }
            } else {
                try (ObjectOutputStream oos = new AppendingObjectOutputStream(
                        new FileOutputStream(arquivo, true))) {
                    oos.writeObject(q);
                }
            }
        } catch (IOException e) {
            throw new RuntimeException("Não foi possível gravar o quarto no arquivo.", e);
        }
    }

    private void reescreverTodos(Map<Integer, Quarto> mapa) {
        ARQUIVO.toFile().delete();
        for (Quarto q : mapa.values()) {
            appendQuarto(q);
        }
    }

    public void save(Quarto q) {
        Map<Integer, Quarto> todos = findAll();
        todos.put(q.getId(), q);
        reescreverTodos(todos);
    }

    public Map<Integer, Quarto> findAll() {
        File arquivo = ARQUIVO.toFile();
        Map<Integer, Quarto> resultado = new HashMap<>();

        if (!arquivo.exists() || arquivo.length() == 0) return resultado;

        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(arquivo))) {
            while (true) {
                try {
                    Quarto q = (Quarto) ois.readObject();
                    resultado.put(q.getId(), q);
                } catch (EOFException e) {
                    break;
                }
            }
        } catch (InvalidClassException e) {
            throw new RuntimeException(
                    "Dados incompatíveis com a versão atual da classe Quarto.", e);
        } catch (StreamCorruptedException e) {
            throw new RuntimeException(
                    "Arquivo corrompido ou não gerado por serialização válida.", e);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(
                    "Classe necessária para reconstruir os objetos não encontrada.", e);
        } catch (IOException e) {
            throw new RuntimeException(
                    "Erro de I/O ao ler o arquivo de quartos.", e);
        }

        return resultado;
    }

    public Quarto findById(int id) {
        return findAll().get(id);
    }

    public boolean deleteById(int id) {
        Map<Integer, Quarto> todos = findAll();
        if (todos.remove(id) == null) return false;
        reescreverTodos(todos);
        return true;
    }
}