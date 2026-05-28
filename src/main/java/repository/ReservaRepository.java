package repository;

import com.sun.source.tree.ReturnTree;
import model.Reserva;
import java.util.*;
import java.io.*;
import java.nio.file.Path;

public class ReservaRepository {
    private static final Path ARQUIVO = Path.of("Reserva.dat");

    private static class AppendingObjectOutputStream extends ObjectOutputStream {
        public AppendingObjectOutputStream(OutputStream out) throws IOException {
            super(out);
        }
        @Override
        protected void writeStreamHeader() throws IOException {
            reset();
        }
    }

    private void appendReserva(Reserva r) {
        File arquivo = ARQUIVO.toFile();
        try {
            if (!arquivo.exists() || arquivo.length() == 0) {
                try (ObjectOutputStream oos = new ObjectOutputStream(
                        new FileOutputStream(arquivo, true))) {
                    oos.writeObject(r);
                }
            } else {
                try (ObjectOutputStream oos = new AppendingObjectOutputStream(
                        new FileOutputStream(arquivo, true))) {
                    oos.writeObject(r);
                }
            }
        } catch (IOException e) {
            throw new RuntimeException("Não foi possível gravar a reserva no arquivo.", e);
        }
    }

    private void reescreverTodos(Map<Integer, Reserva> mapa) {
        ARQUIVO.toFile().delete();
        for (Reserva r : mapa.values()) {
            appendReserva(r);
        }
    }

    public void save(Reserva r) {
        Map<Integer, Reserva> todos = findAll();
        todos.put(r.getId(), r);
        reescreverTodos(todos);
    }

    public Map<Integer, Reserva> findAll() {
        File arquivo = ARQUIVO.toFile();
        Map<Integer, Reserva> resultado = new HashMap<>();

        if (!arquivo.exists() || arquivo.length() == 0) return resultado;

        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(arquivo))) {
            while (true) {
                try {
                    Reserva r = (Reserva) ois.readObject();
                    resultado.put(r.getId(), r);
                } catch (EOFException e) {
                    break;
                }
            }
        } catch (InvalidClassException e) {
            throw new RuntimeException(
                    "Dados incompatíveis com a versão atual da classe Reserva.", e);
        } catch (StreamCorruptedException e) {
            throw new RuntimeException(
                    "Arquivo corrompido ou não gerado por serialização válida.", e);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(
                    "Classe necessária para reconstruir os objetos não encontrada.", e);
        } catch (IOException e) {
            throw new RuntimeException(
                    "Erro de I/O ao ler o arquivo de reservas.", e);
        }

        return resultado;
    }

    public ArrayList<Reserva> findAllByClienteId(Integer clientId) {
        ArrayList<Reserva> resultado = new ArrayList<>();
        for(Reserva r : findAll().values() ){
            if(Objects.equals(r.getClienteId(), clientId)){
                resultado.add(r);
            }
        }
        return resultado;
    }

    public boolean deleteByClienteId(Integer clientId) {

        Map<Integer, Reserva> todos = findAll();
        boolean deleted = todos.values().removeIf(r -> Objects.equals(r.getClienteId(), clientId));
        reescreverTodos(todos);
        return deleted;
    }

    public Reserva findById(int id) {
        return findAll().get(id);
    }

    public boolean deleteById(int id) {
        Map<Integer, Reserva> todos = findAll();
        if (todos.remove(id) == null) return false;
        reescreverTodos(todos);
        return true;
    }

    public boolean existsReservaWithRoom(Integer numQuarto) {
        for (Reserva r : findAll().values()) {
            if  (Objects.equals(numQuarto, r.getNumQuarto())) return true;
        }
        return false;
    }
    public boolean existsByClient(Integer clienteId) {
        for (Reserva r : findAll().values()) {
            if (Objects.equals(r.getClienteId(), clienteId)) return true;
        }
        return false;
    }
}