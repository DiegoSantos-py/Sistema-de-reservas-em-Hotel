package model;

import java.util.Objects;
import java.nio.file.*;

public class Cliente implements java.io.Serializable {
    private final int id;
    private String nome;
    private String cpf;
    private String telefone;
    private static final long serialVersionUID = 1l;

    public Cliente(int id, String nome, String cpf, String telefone){
        this.id = id;
        this.nome = nome;
        this.cpf = cpf;
        this.telefone = telefone;
    }

    public int getId() {return id;
    }

    public String getNome() {return nome;
    }

    public String getCpf() {return cpf;
    }

    public String getTelefone() {return telefone;
    }

    public void setNome(String nome) {this.nome = nome;
    }

    public void setCpf(String cpf) {this.cpf = cpf;
    }

    public void setTelefone(String telefone) {this.telefone = telefone;
    }

    @Override
    public String toString() {
        return String.format("#%d - %S (CPF: %s, Tel: %s)", this.id, this.nome, this.cpf, this.telefone);
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Cliente cliente = (Cliente) o;
        return id == cliente.id;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}
