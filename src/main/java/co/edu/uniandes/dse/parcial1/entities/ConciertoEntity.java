package co.edu.uniandes.dse.parcial1.entities;

import java.time.LocalDateTime;
import java.util.Date;

import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import lombok.Data;
import uk.co.jemos.podam.common.PodamExclude;

@Data
@Entity
public class ConciertoEntity extends BaseEntity {

    private String nombre;
    private Long presupuesto;
    private String artista;
    private LocalDateTime fecha;
    private int aforo;

    @PodamExclude
    @ManyToOne
    private EstadioEntity estadio;
    
}
