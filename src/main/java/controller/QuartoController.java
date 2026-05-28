package controller;

import exceptions.CapacidadeInvalidaException;
import exceptions.IdInvalidoException;
import exceptions.TipoInvalidoException;
import model.Quarto;
import service.ClienteService;
import service.QuartoService;

import java.util.Map;

public class QuartoController {
    private final QuartoService quarto;

    public QuartoController(QuartoService quarto) {
        this.quarto = quarto;
    }

    public Map<Integer, Quarto> listarQuartos() {
        return quarto.listar();
    }

    public void cadastrarQuarto(int id, int codigoTipo, int capacidade)
            throws IdInvalidoException, TipoInvalidoException, CapacidadeInvalidaException {
        quarto.criar(id, codigoTipo, capacidade);
    }

    public void atualizarQuarto(int id, int codigoTipo, int capacidade)
            throws IdInvalidoException, TipoInvalidoException, CapacidadeInvalidaException {
        quarto.atualizar(id, codigoTipo, capacidade);
    }

    public void removerQuarto(int id) throws IdInvalidoException, IllegalStateException {
        quarto.remover(id);
    }
}
