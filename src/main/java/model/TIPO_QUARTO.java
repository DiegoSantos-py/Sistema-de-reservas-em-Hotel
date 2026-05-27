package model;

public enum TIPO_QUARTO {
    solteiro(1),
    casal(2),
    luxo(3),
    suite(4);

     private final int codigo;

    TIPO_QUARTO(int codigo) {
        this.codigo = codigo;
    }

    public int getCodigo() {
        return codigo;
    }

    public static TIPO_QUARTO fromCodigo(int codigo) {
        for (TIPO_QUARTO tipo : TIPO_QUARTO.values()) {
            if (tipo.getCodigo() == codigo) {
                return tipo;
            }
        }
        throw new IllegalArgumentException("Código inválido para tipo de quarto: " + codigo);
    }
}