package pe.edu.lirio.Seda.service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public final class MovimientoCalculations {
    private MovimientoCalculations() {
    }

    public static BigDecimal calcularImporte(Integer cantidad, BigDecimal precioUnidad) {
        if (cantidad == null || cantidad <= 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "La cantidad debe ser mayor que cero");
        }
        if (precioUnidad == null || precioUnidad.signum() < 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "El precio unitario no puede ser negativo ni nulo");
        }
        return precioUnidad.multiply(BigDecimal.valueOf(cantidad)).setScale(2, RoundingMode.HALF_UP);
    }
}