package exception;

public class PesoInvalidoException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public PesoInvalidoException(String mensagem) {
        super(mensagem);
    }
}
