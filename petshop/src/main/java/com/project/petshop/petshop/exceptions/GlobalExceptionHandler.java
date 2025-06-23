package com.project.petshop.petshop.exceptions;

import com.project.petshop.petshop.exceptions.address.AddressAlreadyExist;
import com.project.petshop.petshop.exceptions.address.AddressNotFound;
import com.project.petshop.petshop.exceptions.appointment.AppointmentExist;
import com.project.petshop.petshop.exceptions.appointment.AppointmentNotFound;
import com.project.petshop.petshop.exceptions.breed.BreedAlreadyExist;
import com.project.petshop.petshop.exceptions.breed.BreedNotFound;
import com.project.petshop.petshop.exceptions.contact.ContactAlreadyExist;
import com.project.petshop.petshop.exceptions.contact.ContactNotFound;
import com.project.petshop.petshop.exceptions.pets.PetsAlreadyExist;
import com.project.petshop.petshop.exceptions.pets.PetsNotFound;
import com.project.petshop.petshop.exceptions.user.*;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


/* Classe responsável por capturar e tratar exceções globais na aplicação.
 * Aqui lidamos com exceções específicas e retornamos uma resposta adequada para o cliente
 */
@ControllerAdvice
@RestControllerAdvice
public class GlobalExceptionHandler {

    /*
     * Método genérico que captura a exceção e retorna uma resposta personalizada.
     * @Param ex Exceção.
     * @Param request Objeto que contém informações das requisições usado para obter o caminho da URL.
     * @Return um ResponseEntity contendo os detalhes do erro, incluindo timestamp, status, message e caminho da requisição.
     */
    public ResponseEntity<Map<String, Object>> buildErrorResponse(Exception ex, HttpStatus status, HttpServletRequest request) {
        /* Criamos um map com as informações do erro para retornar ao cliente */
        Map<String, Object> body = Map.of(
                "timestamp", LocalDateTime.now(), /* Momento em que o erro ocorreu */
                "status", status.value(), /* Código HTTP */
                "error", status.getReasonPhrase(), /* Nome do erro */
                "message", ex.getMessage(), /* Mensagem personalizada da exceção */
                "path", request.getRequestURI() /* Caminho da URL onde o erro ocorreu */
        );
        return new ResponseEntity<>(body, status);
    }

    /* USUÁRIO */

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


    /* PETS */

    /* Tratamento para a exceção de conflito de pets e clientes (caso um pet com do mesmo cliente já existir) */
    @ExceptionHandler(PetsAlreadyExist.class)
    public ResponseEntity<Map<String, Object>> handlePetsAlreadyExist(PetsAlreadyExist ex, HttpServletRequest request) {
        return buildErrorResponse(ex, HttpStatus.CONFLICT, request);
    }
    /* Tratamento para exceção de not found (Nenhum pet registrado) */
    @ExceptionHandler(PetsNotFound.class)
    public ResponseEntity<Map<String, Object>> handlePetsNotFound(PetsNotFound ex, HttpServletRequest request) {
        return buildErrorResponse(ex, HttpStatus.NOT_FOUND, request);
    }



    /* BREED */

    @ExceptionHandler(BreedNotFound.class)
    public ResponseEntity<Map<String, Object>> handleBreedNotFound(Exception ex, HttpServletRequest request) {
        return buildErrorResponse(ex, HttpStatus.NOT_FOUND, request);
    }
    @ExceptionHandler(BreedAlreadyExist.class)
    public ResponseEntity<Map<String, Object>> handleBreedAlreadyExist(Exception ex, HttpServletRequest request) {
        return buildErrorResponse(ex, HttpStatus.CONFLICT, request);
    }


    /* CONTACT */

    @ExceptionHandler(ContactAlreadyExist.class)
    public ResponseEntity<Map<String, Object>> handleContactAlreadyExist(Exception ex, HttpServletRequest request) {
        return buildErrorResponse(ex, HttpStatus.CONFLICT, request);
    }
    @ExceptionHandler(ContactNotFound.class)
    public ResponseEntity<Map<String, Object>> handleContactNotFound(Exception ex, HttpServletRequest request) {
        return buildErrorResponse(ex, HttpStatus.NOT_FOUND, request);
    }



    /* ADDRESS */
    @ExceptionHandler(AddressAlreadyExist.class)
    public ResponseEntity<Map<String, Object>> handleAddressAlreadyExist(Exception ex, HttpServletRequest request) {
        return buildErrorResponse(ex, HttpStatus.CONFLICT, request);
    }
    @ExceptionHandler(AddressNotFound.class)
    public ResponseEntity<Map<String, Object>> handleAddressNotFound(Exception ex, HttpServletRequest request) {
        return buildErrorResponse(ex, HttpStatus.NOT_FOUND, request);
    }


    /* Appointment */

    @ExceptionHandler(AppointmentExist.class)
    public ResponseEntity<Map<String, Object>> handleAppointmentExist(Exception ex, HttpServletRequest request) {
        return buildErrorResponse(ex, HttpStatus.CONFLICT, request);
    }
    @ExceptionHandler(AppointmentNotFound.class)
    public ResponseEntity<Map<String, Object>> handleAppointmentNotFound(Exception ex, HttpServletRequest request) {
        return buildErrorResponse(ex, HttpStatus.NOT_FOUND, request);
    }

    /* Authenticate */

    @ExceptionHandler(UnauthorizedException.class)
    public ResponseEntity<Map<String, Object>> handleUnauthorizedException(Exception ex, HttpServletRequest request) {
        return buildErrorResponse(ex, HttpStatus.UNAUTHORIZED, request);
    }
    @ExceptionHandler(InvalidTokenException.class)
    public ResponseEntity<Map<String, Object>> handleInvalidTokenException(Exception ex, HttpServletRequest request) {
        return buildErrorResponse(ex, HttpStatus.UNAUTHORIZED, request);
    }

    @ExceptionHandler(ForbiddenException.class)
    public ResponseEntity<Map<String, Object>> handleForbiddenException(Exception ex, HttpServletRequest request) {
        return buildErrorResponse(ex, HttpStatus.FORBIDDEN, request);
    }


    /* TRATAMENTO DE VALIDAÇÃO DE ENTRADA */

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<List<String>> handleValidationException(MethodArgumentNotValidException ex) {
        List<String> errors = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(FieldError::getDefaultMessage)
                .collect(Collectors.toList());
        return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
    }

}
