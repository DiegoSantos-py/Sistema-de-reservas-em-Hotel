package repository;
import java.io.*;
import model.Cliente;
import java.nio.file.*;

public class ClienteRepository {

    public void save(Cliente c){
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("Cliente.bytej"))){
        oos.writeObject(c);
    } catch (IOException e){
            e.printStackTrace();
        }

    } 

    public List<Cliente> findAll(){
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream("Cliente.bytej"))){
            Cliente c = (Cliente) ois.readObject();
        } catch (IOException | ClassNotFoundException e){
        e.printStackTrace();}
    }

}


/*     public int nextId() { return seq.incrementAndGet(); }

    public Paciente save(Paciente p) { db.put(p.getId(), p); return p; }

    public Optional<Paciente> findById(int id) { return Optional.ofNullable(db.get(id)); }

    public List<Paciente> findAll() { return new ArrayList<>(db.values()); }

    public boolean deleteById(int id) { return db.remove(id) != null; }

    public boolean existsByCpf(String cpf, Integer ignoreId) {
        // any match procura se existe pelo menos um elemento que satisfaz essa condição e retorna true
        return db.values().stream().anyMatch(p ->
                p.getCpf().equalsIgnoreCase(cpf) && (ignoreId == null || p.getId() != ignoreId)
        );
    }
    */