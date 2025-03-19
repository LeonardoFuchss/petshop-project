package com.project.petshop.petshop.exceptions;

import com.project.petshop.petshop.exceptions.pets.PetsAlreadyExist;
import com.project.petshop.petshop.exceptions.pets.PetsNotFound;
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
     * Método genérico que captura a exceção e retorna uma resposta personalizada.
     * @Param ex Exceção.
     * @Param request Objeto que contém informações das requisições usado para obter o caminho da URL.
     * @Return um ResponseEntity contendo os detalhes do erro, incluindo timestamp, status, message e caminho da requisição.
     */
    public ResponseEntity<Map<String, Object>> buildErrorResponse(Exception ex, HttpStatus status, HttpServletRequest request) {
        /* Criamos um mapa com as informações do erro para retornar ao cliente */
        Map<String, Object> body = Map.of(
                "timestamp", LocalDateTime.now(), /* Momento em que o erro ocorreu */
                "status", status.value(), /* Código HTTP */
                "error", status.getReasonPhrase(), /* Nome do erro */
                "message", ex.getMessage(), /* Mensagem personalizada da exceção */
                "path", request.getRequestURI() /* Caminho da URL onde o erro ocorreu */
        );
        return new ResponseEntity<>(body, status);
    }

    /* SESSÃO USUÁRIO */

    /* Tratamento para a exceção de conflito de usuário (usuário já existe) */
    @ExceptionHandler(UserAlreadyException.class)
    public ResponseEntity<Map<String, Object>> handleUserAlreadyException(UserAlreadyException ex, HttpServletRequest request) {
        return buildErrorResponse(ex, HttpStatus.CONFLICT, request);
    }
    /* Tratamento para exceção de not found. (Caso não exista nenhum usuário) */
    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<Map<String, Object>> handleUserNotFoundException(UserNotFoundException ex, HttpServletRequest request) {
        return buildErrorResponse(ex, HttpStatus.NOT_FOUND, request);
    }


    /* SESSÃO PETS */

    /* Tratamento para a exceção de conflito de pets e clientes (caso um pet com do mesmo cliente já exista) */
    @ExceptionHandler(PetsAlreadyExist.class)
    public ResponseEntity<Map<String, Object>> handlePetsAlreadyExist(PetsAlreadyExist ex, HttpServletRequest request) {
        return buildErrorResponse(ex, HttpStatus.CONFLICT, request);
    }
    /* Tratamento para exceção de not found (Nenhum pet registrado) */
    @ExceptionHandler(PetsNotFound.class)
    public ResponseEntity<Map<String, Object>> handlePetsNotFound(PetsNotFound ex, HttpServletRequest request) {
        return buildErrorResponse(ex, HttpStatus.NOT_FOUND, request);
    }
}
