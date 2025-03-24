package com.igorpimentelgameiro.payment_system.error;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class PagamentoErro {
    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<String> error404(){
        var mensagemErro = "Não existe esse registro em nosso banco de dados.";
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(mensagemErro);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<String> error400(MethodArgumentNotValidException ex){
        var mensagemErro = "O dado enviado está incorreto! Verifique os dados enviados pela requisição e tente novamente.";
        return ResponseEntity.badRequest().body(mensagemErro);
    }
}
