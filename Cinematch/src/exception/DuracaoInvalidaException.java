package exception;

public class DuracaoInvalidaException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public DuracaoInvalidaException(String mensagem) {
        super(mensagem);
    }
}
