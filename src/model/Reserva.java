package model;

import java.time.LocalDateTime;
import java.util.Objects;
import java.nio.file.*;

public class Reserva implements java.io.Serializable{
    private final int id;
    private final int clienteId;
    private final int numQuarto;
    private LocalDateTime dataHora;
    private static final long serialVersionUID = 1l;

    public Reserva(int id, int clienteId,LocalDateTime dataHora, int numQuarto) {
        this.id = id;
        this.clienteId = clienteId;
        this.dataHora = dataHora;
        this.numQuarto = numQuarto;
    }

    public int getId() {
        return id;
    }

    public int getClienteId() {
        return clienteId;
    }

    public int getNumQuarto() {
        return numQuarto;
    }

    public LocalDateTime getDataHora() {
        return dataHora;
    }

    public void setDataHora(LocalDateTime dataHora) {
        this.dataHora = dataHora;
    }

    @Override
    public String toString() {
        return String.format("Reserva %d - Cliente: %d (Quarto - %d, Data - %s)",
                this.id, this.clienteId, this.numQuarto, this.dataHora);
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Reserva reserva = (Reserva) o;
        return id == reserva.id;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}
