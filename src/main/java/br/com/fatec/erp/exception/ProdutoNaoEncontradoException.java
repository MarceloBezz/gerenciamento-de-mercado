package br.com.fatec.erp.exception;

public class ProdutoNaoEncontradoException extends Exception{
    public  ProdutoNaoEncontradoException(String mensagem) {
        super(mensagem);
    }
}
