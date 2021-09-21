package br.com.teste.pratico.exception;

public class PersistenceException extends RuntimeException {

	public PersistenceException(String msg) {
		super(msg);
	}
	
	public PersistenceException(Exception ex) {
		super(ex);
	}
	
	public PersistenceException(String msg, Exception ex) {
		super(msg, ex);
	}
	
}
