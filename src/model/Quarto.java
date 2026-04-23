package model;

import java.util.Objects;

public class Quarto {
    private final int id;
    private TIPO_QUARTO tipo;
    private int capacidade;
    private boolean status;

    // Construtor
    public Quarto(int id, TIPO_QUARTO tipo,  int capacidade) {
        this.id = id;
        this.tipo = tipo;
        this.capacidade = capacidade;
    }

    // Getters e Setters
    public int getId() {
        return id;
    }

    public TIPO_QUARTO getTipo() {
        return tipo;
    }

    public void setTipo(TIPO_QUARTO tipo) {
        this.tipo = tipo;
    }

    public int getCapacidade() {
        return capacidade;
    }

    public void setCapacidade(int capacidade) {
        this.capacidade = capacidade;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    // toString
    @Override
    public String toString() {
        return String.format("Quarto %d - Tipo: %s (Tamanho: %s, Capacidade: %d, Disponível: %s)",
                this.id,
                this.tipo,
                this.tamanho,
                this.capacidade,
                this.status ? "Sim" : "Não");
    }

    // equals baseado no id
    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Quarto quarto = (Quarto) o;
        return id == quarto.id;
    }

    // hashCode baseado no id
    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}