package br.com.haroldomorais.librarytest.exception;

import br.com.haroldomorais.librarytest.exception.dto.ErroResponse;
import jakarta.validation.ConstraintViolationException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.util.stream.Collectors;

@RestControllerAdvice
public class GlobalExpetionHandler {

    @ExceptionHandler(ChangeSetPersister.NotFoundException.class)
    public ResponseEntity<ErroResponse> handleNotFound(ChangeSetPersister.NotFoundException e){
        HttpStatus status = HttpStatus.NOT_FOUND;
        String message = defaultMessage(e, "Recurso não encontrado");
        ErroResponse body = new ErroResponse(status.value(), status.getReasonPhrase(), message);
        return new ResponseEntity<>(body, status);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErroResponse> handleValidation(MethodArgumentNotValidException e) {
        String detalhes = e.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(err -> err.getField() + ": " + err.getDefaultMessage())
                .collect(Collectors.joining("; "));
        String message = detalhes.isBlank() ? "Erro de validação nos campos informados" : detalhes;
        HttpStatus status = HttpStatus.BAD_REQUEST;
        ErroResponse body = new ErroResponse(status.value(), status.getReasonPhrase(), message);
        return new ResponseEntity<>(body, status);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ErroResponse> handleConstraintViolation(ConstraintViolationException e) {
        String detalhes = e.getConstraintViolations()
                .stream()
                .map(v -> v.getPropertyPath() + ": " + v.getMessage())
                .collect(Collectors.joining("; "));
        String message = detalhes.isBlank() ? "Parâmetros inválidos" : detalhes;
        HttpStatus status = HttpStatus.BAD_REQUEST;
        ErroResponse body = new ErroResponse(status.value(), status.getReasonPhrase(), message);
        return new ResponseEntity<>(body, status);
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ErroResponse> handleTypeMismatch(MethodArgumentTypeMismatchException e) {
        String required = e.getRequiredType() != null ? e.getRequiredType().getSimpleName() : "tipo esperado";
        String message = String.format("Parâmetro '%s' com tipo inválido. Esperado: %s", e.getName(), required);
        HttpStatus status = HttpStatus.BAD_REQUEST;
        ErroResponse body = new ErroResponse(status.value(), status.getReasonPhrase(), message);
        return new ResponseEntity<>(body, status);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ErroResponse> handleNotReadable(HttpMessageNotReadableException e) {
        HttpStatus status = HttpStatus.BAD_REQUEST;
        ErroResponse body = new ErroResponse(status.value(), status.getReasonPhrase(), "Requisição mal formatada ou JSON inválido");
        return new ResponseEntity<>(body, status);
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ErroResponse> handleConflict(DataIntegrityViolationException e) {
        String causa = e.getMostSpecificCause() != null ? e.getMostSpecificCause().getMessage() : e.getMessage();
        String message = (causa != null && !causa.isBlank()) ? causa : "Violação de integridade dos dados";
        HttpStatus status = HttpStatus.CONFLICT;
        ErroResponse body = new ErroResponse(status.value(), status.getReasonPhrase(), message);
        return new ResponseEntity<>(body, status);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErroResponse> handleIllegalArgument(IllegalArgumentException e) {
        HttpStatus status = HttpStatus.BAD_REQUEST;
        String message = defaultMessage(e, "Requisição inválida");
        ErroResponse body = new ErroResponse(status.value(), status.getReasonPhrase(), message);
        return new ResponseEntity<>(body, status);
    }

    // Captura RuntimeException genérica lançada nos services (ideal: substituir por exceções específicas)
    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ErroResponse> handleRuntime(RuntimeException e) {
        HttpStatus status = HttpStatus.BAD_REQUEST;
        String message = defaultMessage(e, "Operação inválida");
        ErroResponse body = new ErroResponse(status.value(), status.getReasonPhrase(), message);
        return new ResponseEntity<>(body, status);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErroResponse> handleGeneric(Exception e) {
        HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;
        ErroResponse body = new ErroResponse(status.value(), status.getReasonPhrase(), "Ocorreu um erro interno inesperado");
        return new ResponseEntity<>(body, status);
    }

    private String defaultMessage(Throwable e, String fallback){
        String msg = e.getMessage();
        return (msg == null || msg.isBlank()) ? fallback : msg;
    }
}
