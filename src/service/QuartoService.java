package service;

import java.util.Map;

import exceptions.CapacidadeInvalidaException;
import exceptions.IdInvalidoException;
import exceptions.TipoInvalidoException;
import model.Quarto;
import model.TIPO_QUARTO;
import repository.QuartoRepository;

public class QuartoService implements java.io.Serializable {
    private final QuartoRepository repo;

    public QuartoService(QuartoRepository repo) {
        this.repo = repo;
    }

    public Map<Integer, Quarto> listar() {
        return repo.findAll();
    }

    public Quarto buscarPorId(int id) {
        return repo.findById(id);
    }

    public Quarto criar(Integer id, int tipo, Integer capacidade)
            throws IdInvalidoException, TipoInvalidoException, CapacidadeInvalidaException {
        validarIdParaCriacao(id);
        validarTipo(tipo);
        validarCapacidade(capacidade);

        Quarto quarto = new Quarto(id, TIPO_QUARTO.fromCodigo(tipo), capacidade);
        repo.save(quarto);
        return quarto;
    }

    public Quarto atualizar(Integer id, int tipo, Integer capacidade)
            throws IdInvalidoException, TipoInvalidoException, CapacidadeInvalidaException {
        validarIdExistente(id);
        validarTipo(tipo);
        validarCapacidade(capacidade);

        Quarto q = repo.findById(id);
        q.setTipo(TIPO_QUARTO.fromCodigo(tipo));
        q.setCapacidade(capacidade);
        repo.save(q);
        return q;
    }

    public boolean remover(Integer id) throws IdInvalidoException {
        validarIdExistente(id);
        return repo.deleteById(id);
    }

    private TIPO_QUARTO validarTipo(int codigo) throws TipoInvalidoException {
    try {
        return TIPO_QUARTO.fromCodigo(codigo);

    } catch (IllegalArgumentException e) {
        throw new TipoInvalidoException(
            "Tipo de quarto inválido para o código: " + codigo
        );

    } finally {
        System.out.println("Validação do tipo de quarto executada.");
    }
}

    private void validarCapacidade(Integer capacidade) throws CapacidadeInvalidaException {
        int capacidadeMax = 7;
        int capacidadeMin = 1;

        try {
            if (capacidade == null) {
                throw new NullPointerException();
            }

            if (capacidade < capacidadeMin) {
                throw new IllegalArgumentException();
            }

            if(capacidade > capacidadeMax) {throw new IllegalArgumentException();}

        } catch (NullPointerException e) {
            throw new CapacidadeInvalidaException("Capacidade não pode ser nula.");

        } catch (IllegalArgumentException e) {
            throw new CapacidadeInvalidaException(
                "Capacidade inválida: " + capacidade +
                ". Deve estar entre " + capacidadeMin + " e " + capacidadeMax + "."
            );

        } finally {
            System.out.println("Validação de capacidade executada.");
        }
    }

    private void validarIdParaCriacao(Integer id) throws IdInvalidoException {
        try {
            Map<Integer, Quarto> quartos = listar();

            if (id == null) {
                throw new NullPointerException();
            }

            if (quartos.containsKey(id)) {
                throw new IllegalArgumentException();
            }

        } catch (NullPointerException e) {
            throw new IdInvalidoException("Id não pode ser nulo.");

        } catch (IllegalArgumentException e) {
            throw new IdInvalidoException(
                "O Id fornecido já está associado a um quarto: " + id
            );

        } finally {
            System.out.println("Validação de id para criação executada.");
        }
    }

    private void validarIdExistente(Integer id) throws IdInvalidoException {
        try {
            Map<Integer, Quarto> quartos = listar();

            if (id == null) {
                throw new NullPointerException();
            }

            if (!quartos.containsKey(id)) {
                throw new IllegalArgumentException();
            }

        } catch (NullPointerException e) {
            throw new IdInvalidoException("Id não pode ser nulo.");

        } catch (IllegalArgumentException e) {
            throw new IdInvalidoException(
                "Não existe quarto cadastrado com o id: " + id
            );

        } finally {
            System.out.println("Validação de id existente executada.");
        }
    }
}