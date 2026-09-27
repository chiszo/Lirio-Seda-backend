package pe.edu.lirio.Seda.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import pe.edu.lirio.Seda.model.dto.MensajeErrorDto;

import java.util.Date;

@RestControllerAdvice
public class ControllerExceptionHandler {
    @ExceptionHandler(ResourceNotFoundException.class)
    @ResponseStatus(value = HttpStatus.NOT_FOUND)
    public MensajeErrorDto resourceNotFoundException(
            ResourceNotFoundException ex,
            WebRequest request
    ) {
        return MensajeErrorDto.builder().mensaje(ex.getMessage())
                .codigoEstado(HttpStatus.NOT_FOUND.value())
                .fechaError(new Date())
                .descripcion(request.getDescription(true)).build();
    }
}