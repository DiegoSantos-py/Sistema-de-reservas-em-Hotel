package service;

import java.util.Map;

import exceptions.CapacidadeInvalidaException;
import exceptions.IdInvalidoException;
import exceptions.TipoInvalidoException;
import model.Quarto;
import model.TIPO_QUARTO;
import repository.QuartoRepository;

public class QuartoService {                            // removido Serializable — service não é serializável
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

    // -------------------------------------------------------------------------
    // Validações
    // -------------------------------------------------------------------------

    private TIPO_QUARTO validarTipo(int codigo) throws TipoInvalidoException {
        // removido o try/catch/finally — o único propósito do finally era um println de debug
        // que executava mesmo em caso de exceção, poluindo o log sem agregar valor
        try {
            return TIPO_QUARTO.fromCodigo(codigo);
        } catch (IllegalArgumentException e) {
            throw new TipoInvalidoException("Tipo de quarto inválido para o código: " + codigo);
        }
    }

    private void validarCapacidade(Integer capacidade) throws CapacidadeInvalidaException {
        // removidos os try/catch desnecessários — NullPointerException e
        // IllegalArgumentException eram lançados manualmente só para serem relançados
        // como outra exceção; if/else direto é mais legível e sem overhead
        if (capacidade == null) {
            throw new CapacidadeInvalidaException("Capacidade não pode ser nula.");
        }
        if (capacidade < 1 || capacidade > 7) {
            throw new CapacidadeInvalidaException(
                    "Capacidade inválida: " + capacidade + ". Deve estar entre 1 e 7."
            );
        }
    }

    private void validarIdParaCriacao(Integer id) throws IdInvalidoException {
        if (id == null) {
            throw new IdInvalidoException("Id não pode ser nulo.");
        }
        if (repo.findById(id) != null) {             // consulta direta em vez de carregar o Map inteiro
            throw new IdInvalidoException("O Id fornecido já está associado a um quarto: " + id);
        }
    }

    private void validarIdExistente(Integer id) throws IdInvalidoException {
        if (id == null) {
            throw new IdInvalidoException("Id não pode ser nulo.");
        }
        if (repo.findById(id) == null) {             // idem
            throw new IdInvalidoException("Não existe quarto cadastrado com o id: " + id);
        }
    }
}