package br.ufc.quimica.keyboard.parser;

/**
 * Syntax Error Exception.
 * @author vladymirbezerra
 *
 */
public class SyntaxErrorException extends RuntimeException {
	
	public SyntaxErrorException(String msg) {
		super(msg);
	}
}
