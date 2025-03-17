package com.project.petshop.petshop.exceptions;

import com.project.petshop.petshop.exceptions.user.UserAlreadyException;
import com.project.petshop.petshop.exceptions.user.UserNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.LocalDateTime;
import java.util.Map;


/* Classe responsável por capturar e tratar exceções globais na aplicação.
 * Aqui lidamos com exceções específicas e retornamos uma resposta adequada para o cliente
 */
@ControllerAdvice
public class GlobalExceptionHandler {

    /*
     * Método que captura a exceção UserAlreadyException e retorna uma resposta personalizada.
     * @Param ex Exceção lançada quando o usuário já existe no sistema.
     * @Param request Objeto que contém informações das requisições usado para obter o caminho da URL.
     * @Return um ResponseEntity contendo os detalhes do erro, incluindo timestamp, status, message e caminho da requisição.
     */
    @ExceptionHandler(UserAlreadyException.class)
    public ResponseEntity<Map<String, Object>> handleUserAlreadyException(UserAlreadyException ex, HttpServletRequest request) {
        /* Criamos um mapa com as informações do erro para retornar ao cliente */
        Map<String, Object> body = Map.of(
                "timestamp", LocalDateTime.now(), /* Momento em que o erro ocorreu */
                "status", HttpStatus.CONFLICT.value(), /* Código HTTP 409 - Conflito */
                "error", "Conflict", /* Nome do erro */
                "message", ex.getMessage(), /* Mensagem personalizada da exceção */
                "path", request.getRequestURI() /* Caminho da URL onde o erro ocorreu */
        );
        return new ResponseEntity<>(body, HttpStatus.CONFLICT);
    }
    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<Map<String, Object>> handleUserNotFoundException(UserNotFoundException ex, HttpServletRequest request) {
        /* Criamos um mapa com as informações do erro para retornar ao cliente */
        Map<String, Object> body = Map.of(
                "timestamp", LocalDateTime.now(), /* Momento em que o erro ocorreu */
                "status", HttpStatus.NOT_FOUND.value(), /* Código HTTP 404 - Conflito */
                "error", "NOT_FOUND", /* Nome do erro */
                "message", ex.getMessage(), /* Mensagem personalizada da exceção */
                "path", request.getRequestURI() /* Caminho da URL onde o erro ocorreu */
        );
        return new ResponseEntity<>(body, HttpStatus.NOT_FOUND);
    }
}
