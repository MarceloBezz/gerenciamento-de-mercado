package br.com.fatec.erp.exception;

public class LoteInvalidoException extends RuntimeException{
    public  LoteInvalidoException(String mensagem) {
        super(mensagem);
    }
}