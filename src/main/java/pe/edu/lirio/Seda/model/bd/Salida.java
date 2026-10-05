package pe.edu.lirio.Seda.model.bd;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "Salida")
public class Salida {
    @Id
    @Column(name = "idsalida", length = 10)
    private String idSalida;

    @Column(name = "fechasalida")
    private LocalDate fechaSalida;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "Usuario_idusuario", nullable = false)
    private Usuarios usuario;

    @Column(name = "idsedeusuario")
    private Integer idSedeUsuario;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "Motivo_idmotivo", nullable = false)
    private Motivo motivo;

    @OneToMany(mappedBy = "salida", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<DetalleSalida> detalles = new ArrayList<>();
}