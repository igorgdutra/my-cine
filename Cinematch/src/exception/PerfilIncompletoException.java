package exception;

public class PerfilIncompletoException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public PerfilIncompletoException(String mensagem) {
		super(mensagem);
	}
}
